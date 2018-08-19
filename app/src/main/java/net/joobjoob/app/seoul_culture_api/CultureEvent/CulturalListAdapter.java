//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.joobjoob.app.seoul_culture_api.CultureEvent;
import net.joobjoob.app.seoul_culture_api.Common.CulturalInfo;
import net.joobjoob.app.seoul_culture_api.Common.FontUtils;
import net.joobjoob.app.seoul_culture_api.R.id;
import net.joobjoob.app.seoul_culture_api.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



// comment: 문화정보 리스트 Adapter
class CulturalListAdapter extends ArrayAdapter<CulturalInfo> {
    private CulturalListAdapter.ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private Context mContext = null;

    public CulturalListAdapter(Context c, int ResourceId, ArrayList<CulturalInfo> arrays) {
        super(c, ResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public int getCount() {
        return super.getCount();
    }

    public CulturalInfo getItem(int position) {
        return (CulturalInfo)super.getItem(position);
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }


    // comment: getView func
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;
        if(convertview == null) {
            this.viewHolder = new CulturalListAdapter.ViewHolder();
            v = this.inflater.inflate(layout.cultural_event_list_item_type, (ViewGroup)null);
            this.viewHolder.dDay = (TextView)v.findViewById(id.cultural_d_day);
            this.viewHolder.mainImg = (ImageView)v.findViewById(id.main_img);
            this.viewHolder.title = (TextView)v.findViewById(id.cultural_title);
            this.viewHolder.date = (TextView)v.findViewById(id.cultural_date);
            this.viewHolder.gCode = (TextView)v.findViewById(id.cultural_g_code);
            v.setTag(this.viewHolder);
        } else {
            this.viewHolder = (CulturalListAdapter.ViewHolder)convertview.getTag();
        }

        this.viewHolder.dDay.setText(this.getDday(this.getItem(position).getSTRTDATE()) + "");
        this.viewHolder.title.setText(this.getItem(position).getTITLE());
        this.viewHolder.date.setText(this.getItem(position).getSTRTDATE() + "~" + this.getItem(position).getEND_DATE());
        this.viewHolder.gCode.setText(this.getItem(position).getGCODE());
        String url = "";
        String[] mainImg = this.getItem(position).getMAIN_IMG().split("\\/");

        for(int i = 0; i < mainImg.length; ++i) {
            if(i != 0 && i != 1 && i != 2) {
                if(i == mainImg.length - 1) {
                    url = url + mainImg[i];
                } else {
                    url = url + mainImg[i] + "/";
                }
            } else {
                url = url + mainImg[i].toLowerCase() + "/";
            }
        }

        if(!this.getItem(position).getMAIN_IMG().equals("")) {
             //Glide.with(this.mContext).load(url).error(R.drawable.bg_bigimg).into(this.viewHolder.mainImg);
            // change: bg_bigimg를 인식못해서 일단 주석처리해놓았는데 여기 부분처리할 방법을 알아내야함

            Glide.with(this.mContext).load(url).into(this.viewHolder.mainImg);

        }

        FontUtils.getInstance(this.mContext).setGlobalFont(parent);
        return v;
    }
    // comment : GetDate func
    public String getDday(String startDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(startDate);
            Date currentDay = formatter.parse(formatter.format(new Date()));
            long diff = beginDate.getTime() - currentDay.getTime();
            long diffDays = diff / 86400000L;
            return diffDays <= 0L?"진행중":"D-" + diffDays;
        } catch (ParseException var9) {
            var9.printStackTrace();
            return "";
        }
    }
    // comment: Viewholder Class model
    class ViewHolder {
        public ImageView mainImg;
        public TextView dDay;
        public TextView title;
        public TextView date;
        public TextView gCode;

        ViewHolder() {
        }
    }
}
