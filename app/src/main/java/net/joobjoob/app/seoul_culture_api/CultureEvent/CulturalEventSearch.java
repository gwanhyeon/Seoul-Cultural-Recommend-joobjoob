//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.joobjoob.app.seoul_culture_api.CultureEvent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import net.joobjoob.app.seoul_culture_api.Common.BaseActivity;
import net.joobjoob.app.seoul_culture_api.Common.ConsertSubjectCatalogInfo;
import net.joobjoob.app.seoul_culture_api.Common.CulturalInfo;
import net.joobjoob.app.seoul_culture_api.Common.CustomProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.joobjoob.app.seoul_culture_api.Common.FontUtils;

import net.joobjoob.app.seoul_culture_api.R;
import net.joobjoob.app.seoul_culture_api.R.id;
import net.joobjoob.app.seoul_culture_api.R.layout;


// comment : 실질적으로 지금 현재 메인 호출하는부분 search 화면

public class CulturalEventSearch extends BaseActivity {
    private String[] categoryName = new String[]{"명칭", "기간", "장르"};
    private ConsertSubjectCatalogInfo[] concertSubjectCatalogList;
    private String[] concertSubjectCatalogNameList;
    private AlertDialog alert;
    private int selectedCategory = 0;
    private int selectedSubCodeCatalog = 0;
    private ListView culturalListview;
    private LinearLayout btnNameLoadMore;
    private LinearLayout btnPeriodLoadMore;
    private LinearLayout btnSubCodeCatalogLoadMore;
    private ArrayList<CulturalInfo> culturalInfoArrayList = new ArrayList();
    private CulturalListAdapter culturalListAdapter;
    private int startNo = -1;
    private int selectCategory = -1;
    private int selectCatalog = -1;
    private String searchKey;
    private String startDate;
    private String endDate;
    private CustomProgressDialog dialogLoading;
    private String openAPIKey = "";

    public CulturalEventSearch() {
    }

    // comment : openApi call
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.cultural_event_search_type);
        if(this.getIntent() != null && this.getIntent().getStringExtra("OpenAPIKey") != null) {
            this.openAPIKey = this.getIntent().getStringExtra("OpenAPIKey");
        } else {
            openAPIKey = getResources().getString(R.string.get_open_api_key);
        }

        this.initView();
    }
    //comment : init setting func
    private void initView() {
        this.culturalListview = (ListView)this.findViewById(id.cultural_listview);
        this.culturalListAdapter = new CulturalListAdapter(this, layout.cultural_event_list_item_type, this.culturalInfoArrayList);
        this.culturalListview.setAdapter(this.culturalListAdapter);
        this.culturalListview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CulturalEventSearch.this, CulturalEventDetail.class);
                intent.putExtra("CulturalInfo", (CulturalInfo)adapterView.getAdapter().getItem(i));
                CulturalEventSearch.this.startActivity(intent);
            }
        });
    }

    // comment : 맨위로 올리는부분 TOP func
    public void goTop(View view) {
        this.culturalListview.smoothScrollToPosition(0);
    }


    // comment : progress dialog show
    public void showProgressDialog() {
        if(this.dialogLoading == null && !this.isFinishing()) {
            this.dialogLoading = CustomProgressDialog.show(this, "", "");
            this.dialogLoading.setCancelable(false);
        }

        if(this.dialogLoading != null && !this.dialogLoading.isShowing()) {
            this.dialogLoading.show();
        }

    }
    // comment : cancel progress dialog func

    public void cancelProgressDialog() {
        if(this.dialogLoading != null && this.dialogLoading.isShowing()) {
            this.dialogLoading.cancel();
        }

    }

    // comment : Search name func
    public void searchName(View view) {
        this.searchKey = ((EditText)this.findViewById(id.search_concert_name)).getText().toString();
        if(this.searchKey.length() != 0 && !this.searchKey.contains(" ")) {
            this.culturalInfoArrayList.clear();
            this.culturalListAdapter.notifyDataSetChanged();
            this.startNo = 1;
            this.showProgressDialog();
            (new CulturalEventSearch.ProcessNetworkSearchConcertNameThread()).execute(new String[]{this.searchKey});
            if(this.btnNameLoadMore != null) {
                this.culturalListview.removeFooterView(this.btnNameLoadMore);
                this.btnNameLoadMore = null;
            }

            if(this.btnNameLoadMore == null) {
                LayoutInflater li = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.btnNameLoadMore = (LinearLayout)li.inflate(layout.btn_load_more, (ViewGroup)null);
                FontUtils.getInstance(this).setGlobalFont(this.btnNameLoadMore);
                this.btnNameLoadMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if(CulturalEventSearch.this.culturalListview.getCount() > 0) {
                            CulturalEventSearch.this.showProgressDialog();
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertNameThread()).execute(new String[]{CulturalEventSearch.this.searchKey});
                        }

                    }
                });
            }

            this.culturalListview.addFooterView(this.btnNameLoadMore);
        } else {
            Builder alert_confirm = new Builder(this, 3);
            alert_confirm.setMessage("공백을 포함하지 않는\n검색어를 입력해 주십시오.").setNegativeButton("확인", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.alert = alert_confirm.create();
            this.alert.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    CulturalEventSearch.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
                }
            });
            this.alert.show();
        }

    }
    // comment : ShowDatePickerDialog

    public void showDatePickerDialog(final View view) {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        (new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d%d%d", new Object[]{Integer.valueOf(year), Integer.valueOf(monthOfYear + 1), Integer.valueOf(dayOfMonth)});
                ((TextView)view).setText(msg);
            }
        }, year, month, day)).show();
    }

    // comment : SearchPeriod
    public void searchPeriod(View view) {
        this.startDate = ((TextView)this.findViewById(id.start_date)).getText().toString();
        this.endDate = ((TextView)this.findViewById(id.end_date)).getText().toString();
        if(this.startDate.length() != 0 && this.endDate.length() != 0) {
            this.culturalInfoArrayList.clear();
            this.culturalListAdapter.notifyDataSetChanged();
            this.startNo = 1;
            this.showProgressDialog();
            (new CulturalEventSearch.ProcessNetworkSearchConcertPeriodThread()).execute(new String[]{this.startDate, this.endDate});
            if(this.btnPeriodLoadMore != null) {
                this.culturalListview.removeFooterView(this.btnPeriodLoadMore);
                this.btnPeriodLoadMore = null;
            }

            if(this.btnPeriodLoadMore == null) {
                LayoutInflater li = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.btnPeriodLoadMore = (LinearLayout)li.inflate(layout.btn_load_more, (ViewGroup) null);
                FontUtils.getInstance(this).setGlobalFont(this.btnPeriodLoadMore);
                this.btnPeriodLoadMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if(CulturalEventSearch.this.culturalListview.getCount() > 0) {
                            CulturalEventSearch.this.showProgressDialog();
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertPeriodThread()).execute(new String[]{CulturalEventSearch.this.startDate, CulturalEventSearch.this.endDate});
                        }

                    }
                });
            }

            this.culturalListview.addFooterView(this.btnPeriodLoadMore);
        } else {
            Builder alert_confirm = new Builder(this, 3);
            alert_confirm.setMessage("검색기간을 입력해 주십시오.").setNegativeButton("확인", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.alert = alert_confirm.create();
            this.alert.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    CulturalEventSearch.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
                }
            });
            this.alert.show();
        }

    }
    // comment : Culture show more
    public void showSubCodeCatalog(View view) {
        Builder alert_confirm = new Builder(this, 3);
        ((ImageView)this.findViewById(id.category_subcode_selector)).setSelected(true);
        alert_confirm.setSingleChoiceItems(this.concertSubjectCatalogNameList, this.selectedSubCodeCatalog, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CulturalEventSearch.this.selectedSubCodeCatalog = whichButton;
            }
        }).setPositiveButton("확인", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(CulturalEventSearch.this.culturalInfoArrayList != null && CulturalEventSearch.this.selectedSubCodeCatalog != CulturalEventSearch.this.selectCatalog) {
                    if(CulturalEventSearch.this.btnSubCodeCatalogLoadMore != null) {
                        CulturalEventSearch.this.culturalListview.removeFooterView(CulturalEventSearch.this.btnSubCodeCatalogLoadMore);
                        CulturalEventSearch.this.btnSubCodeCatalogLoadMore = null;
                    }

                    CulturalEventSearch.this.culturalInfoArrayList.clear();
                    CulturalEventSearch.this.culturalListAdapter.notifyDataSetChanged();
                    ((TextView)CulturalEventSearch.this.findViewById(id.category_subcode_name)).setText(CulturalEventSearch.this.concertSubjectCatalogNameList[CulturalEventSearch.this.selectedSubCodeCatalog]);
                    CulturalEventSearch.this.startNo = 1;
                    CulturalEventSearch.this.selectCatalog = CulturalEventSearch.this.selectedSubCodeCatalog;
                    CulturalEventSearch.this.showProgressDialog();
                    (CulturalEventSearch.this.new ProcessNetworkSearchPerformanceBySubjectThread()).execute(new String[]{CulturalEventSearch.this.concertSubjectCatalogList[CulturalEventSearch.this.selectedSubCodeCatalog].getCODE()});


                    if(CulturalEventSearch.this.btnSubCodeCatalogLoadMore == null) {
                        LayoutInflater li = (LayoutInflater)CulturalEventSearch.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        CulturalEventSearch.this.btnSubCodeCatalogLoadMore = (LinearLayout)li.inflate(layout.btn_load_more, (ViewGroup)null);
                        FontUtils.getInstance(CulturalEventSearch.this).setGlobalFont(CulturalEventSearch.this.btnSubCodeCatalogLoadMore);
                        CulturalEventSearch.this.btnSubCodeCatalogLoadMore.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                if(CulturalEventSearch.this.culturalListview.getCount() > 0) {
                                    CulturalEventSearch.this.showProgressDialog();
                                    (CulturalEventSearch.this.new ProcessNetworkSearchPerformanceBySubjectThread()).execute(new String[]{CulturalEventSearch.this.concertSubjectCatalogList[CulturalEventSearch.this.selectCatalog].getCODE()});
                                }

                            }
                        });
                    }

                    CulturalEventSearch.this.culturalListview.addFooterView(CulturalEventSearch.this.btnSubCodeCatalogLoadMore);
                }

            }
        }).setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        this.alert = alert_confirm.create();
        this.alert.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                FontUtils.getInstance(CulturalEventSearch.this).setGlobalFont(CulturalEventSearch.this.alert.getListView());
                CulturalEventSearch.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
                CulturalEventSearch.this.alert.getButton(-1).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
            }
        });
        this.alert.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ((ImageView)CulturalEventSearch.this.findViewById(id.category_subcode_selector)).setSelected(false);
            }
        });
        this.alert.show();
    }

    // comment : category Show
    public void showCategory(View view) {
        Builder alert_confirm = new Builder(this, 3);
        ((ImageView)this.findViewById(id.category_selector)).setSelected(true);
        alert_confirm.setSingleChoiceItems(this.categoryName, this.selectedCategory, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CulturalEventSearch.this.selectedCategory = whichButton;
            }
        }).setPositiveButton("확인", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(CulturalEventSearch.this.selectCategory != CulturalEventSearch.this.selectedCategory) {
                    CulturalEventSearch.this.initCondition();
                    ((TextView)CulturalEventSearch.this.findViewById(id.category)).setText(CulturalEventSearch.this.categoryName[CulturalEventSearch.this.selectedCategory]);
                    CulturalEventSearch.this.findViewById(id.layout_category_0).setVisibility(Integer.parseInt("8"));
                    CulturalEventSearch.this.findViewById(id.layout_category_1).setVisibility(Integer.parseInt("8"));
                    CulturalEventSearch.this.findViewById(id.layout_category_2).setVisibility(Integer.parseInt("8"));

                    // comment category View
                    switch(CulturalEventSearch.this.selectedCategory) {
                        case 0:
                            CulturalEventSearch.this.findViewById(id.layout_category_0).setVisibility(Integer.parseInt("0"));
                            break;
                        case 1:
                            CulturalEventSearch.this.findViewById(id.layout_category_1).setVisibility(Integer.parseInt("0"));
                            break;
                        case 2:
                            CulturalEventSearch.this.findViewById(id.layout_category_2).setVisibility(Integer.parseInt("0"));
                            CulturalEventSearch.this.showProgressDialog();
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertSubjectCatalogThread()).execute(new String[0]);
                    }

                    CulturalEventSearch.this.selectCategory = CulturalEventSearch.this.selectedCategory;
                }

            }
        }).setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        this.alert = alert_confirm.create();
        this.alert.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                FontUtils.getInstance(CulturalEventSearch.this).setGlobalFont(CulturalEventSearch.this.alert.getListView());
                CulturalEventSearch.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
                CulturalEventSearch.this.alert.getButton(-1).setTypeface(FontUtils.getInstance(CulturalEventSearch.this).getmTypeface());
            }
        });
        this.alert.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ((ImageView)CulturalEventSearch.this.findViewById(id.category_selector)).setSelected(false);
            }
        });
        this.alert.show();
        this.hideSoftKeyboard();
    }
    // comment: Culture Category (장르 리스트 문화 예술 )
    private void initCondition() {
        this.startNo = 1;
        this.selectCatalog = -1;
        ((TextView)this.findViewById(id.category_subcode_name)).setText("장르를 선택해 주세요.");
        if(this.culturalInfoArrayList != null) {
            this.culturalInfoArrayList.clear();
        }

        if(this.culturalListAdapter != null) {
            this.culturalListAdapter.notifyDataSetChanged();
        }

        if(this.btnNameLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnNameLoadMore);
            this.btnNameLoadMore = null;
        }

        if(this.btnPeriodLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnPeriodLoadMore);
            this.btnPeriodLoadMore = null;
        }

        if(this.btnSubCodeCatalogLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnSubCodeCatalogLoadMore);
            this.btnSubCodeCatalogLoadMore = null;
        }

    }
    // comment :App softeKeyBoard func
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((EditText)this.findViewById(id.search_concert_name)).getWindowToken(), 0);
    }

    // comment 문화 정보 검색시 세부사항 가져오는 AsyncTask
    public class ProcessNetworkSearchConcertDetailThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertDetailThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }
        // comment : 서울 문화 포털에서 JSON -> HTTP (HTTP URL : http://culture.seoul.go.kr/data)
        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertDetailService");
                JSONArray row = jsonObject.getJSONArray("row");
                if(row.length() > 0) {
                    String title = row.getJSONObject(0).getString("TITLE");
                    title = title.replaceAll("<", "&lt;");
                    title = title.replaceAll(">", "&gt;");
                    CulturalEventSearch.this.culturalInfoArrayList.add(new CulturalInfo(row.getJSONObject(0).getString("CULTCODE"), row.getJSONObject(0).getString("SUBJCODE"), row.getJSONObject(0).getString("CODENAME"), Html.fromHtml(title).toString(), row.getJSONObject(0).getString("STRTDATE"), row.getJSONObject(0).getString("END_DATE"), row.getJSONObject(0).getString("TIME"), row.getJSONObject(0).getString("PLACE"), row.getJSONObject(0).getString("ORG_LINK"), row.getJSONObject(0).getString("MAIN_IMG"), row.getJSONObject(0).getString("HOMEPAGE"), row.getJSONObject(0).getString("USE_TRGT"), row.getJSONObject(0).getString("USE_FEE"), row.getJSONObject(0).getString("SPONSOR"), row.getJSONObject(0).getString("INQUIRY"), row.getJSONObject(0).getString("SUPPORT"), row.getJSONObject(0).getString("ETC_DESC"), row.getJSONObject(0).getString("AGELIMIT"), row.getJSONObject(0).getString("IS_FREE"), row.getJSONObject(0).getString("TICKET"), row.getJSONObject(0).getString("PROGRAM"), row.getJSONObject(0).getString("PLAYER"), row.getJSONObject(0).getString("CONTENTS"), row.getJSONObject(0).getString("GCODE")));
                    System.out.println("************************************************************************"+row.getJSONObject(0).getString("CULTCODE") + row.getJSONObject(0).getString("MAIN_IMG"));


                }

                CulturalEventSearch.this.culturalListAdapter.notifyDataSetChanged();
            } catch (JSONException var7) {
                var7.printStackTrace();
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearch.this.openAPIKey + "/json/SearchConcertDetailService/1/1/" + strings[0]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
    // comment : 문화정보 검색시 Performance담당하는 AsyncTask
    public class ProcessNetworkSearchPerformanceBySubjectThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchPerformanceBySubjectThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if(jsonMain.has("SearchPerformanceBySubjectService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchPerformanceBySubjectService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if(row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearch.this.startNo = CulturalEventSearch.this.startNo + 20;
                } else {
                    CulturalEventSearch.this.startNo = -1;
                    CulturalEventSearch.this.cancelProgressDialog();
                    CulturalEventSearch.this.btnSubCodeCatalogLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearch.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearch.this.openAPIKey + "/json/SearchPerformanceBySubjectService/" + CulturalEventSearch.this.startNo + "/" + (CulturalEventSearch.this.startNo + 19) + "/" + strings[0]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
    // comment: AsyncTask Concert 문화정보 카테고리 작업 처리
    public class ProcessNetworkSearchConcertSubjectCatalogThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertSubjectCatalogThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertSubjectCatalogService");
                String listTotalCount = jsonObject.getString("list_total_count");
                CulturalEventSearch.this.concertSubjectCatalogList = new ConsertSubjectCatalogInfo[Integer.parseInt(listTotalCount)];
                CulturalEventSearch.this.concertSubjectCatalogNameList = new String[Integer.parseInt(listTotalCount)];
                JSONArray row = jsonObject.getJSONArray("row");

                for(int i = 0; i < row.length(); ++i) {
                    CulturalEventSearch.this.concertSubjectCatalogList[i] = new ConsertSubjectCatalogInfo(row.getJSONObject(i).getString("CODE"), row.getJSONObject(i).getString("CODENAME"));
                    CulturalEventSearch.this.concertSubjectCatalogNameList[i] = row.getJSONObject(i).getString("CODENAME");
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                CulturalEventSearch.this.cancelProgressDialog();
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearch.this.openAPIKey + "/json/SearchConcertSubjectCatalogService/1/999");

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
    // comment : AsyncTask 문화정보 기간 확인할때사용하는 Thread
    public class ProcessNetworkSearchConcertPeriodThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertPeriodThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);

            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if(jsonMain.has("SearchConcertPeriodService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertPeriodService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if(row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearch.this.startNo = CulturalEventSearch.this.startNo + 20;
                } else {
                    CulturalEventSearch.this.startNo = -1;
                    CulturalEventSearch.this.cancelProgressDialog();
                    CulturalEventSearch.this.btnPeriodLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearch.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearch.this.openAPIKey + "/json/SearchConcertPeriodService/" + CulturalEventSearch.this.startNo + "/" + (CulturalEventSearch.this.startNo + 19) + "/" + strings[0] + "/" + strings[1]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }


    // comment : AsyncTask 문화정보 찾을때쓰레드 작업
    public class ProcessNetworkSearchConcertNameThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertNameThread() {
        }

        protected String doInBackground(String... strings) {

            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if(jsonMain.has("SearchConcertNameService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertNameService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if(row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearch.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearch.this.startNo = CulturalEventSearch.this.startNo + 20;
                } else {
                    CulturalEventSearch.this.startNo = -1;
                    CulturalEventSearch.this.cancelProgressDialog();
                    CulturalEventSearch.this.btnNameLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearch.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }
        // comment:  실행전 파라미터 받아서 로딩이미지 스레드작업 하는곳
        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearch.this.openAPIKey + "/json/SearchConcertNameService/" + CulturalEventSearch.this.startNo + "/" + (CulturalEventSearch.this.startNo + 19) + "/" + strings[0] + "/");

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
}
