package com.oneMap.module.Baidu.main.adapter;

import android.content.Context;

import com.baidu.mapapi.search.core.PoiInfo;
import com.oneMap.module.Baidu.R;
import com.oneMap.module.Baidu.main.adapter.base.BaseViewHolder;
import com.oneMap.module.Baidu.main.adapter.base.MyBaseAdapter;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 */
public class NearAddressAdapter extends MyBaseAdapter<PoiInfo> {

	public NearAddressAdapter(Context context, int resource, List<PoiInfo> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, PoiInfo info) {
		viewHolder.setTextView(R.id.item_address_name_tv, info.name);
		viewHolder.setTextView(R.id.item_address_detail_tv, info.address);
	}

}
