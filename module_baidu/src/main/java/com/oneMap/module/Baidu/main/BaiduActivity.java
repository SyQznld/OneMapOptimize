package com.oneMap.module.Baidu.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.oneMap.module.Baidu.R;
import com.oneMap.module.Baidu.main.adapter.NearAddressAdapter;
import com.oneMap.module.Baidu.main.adapter.SearchAddressAdapter;
import com.oneMap.module.Baidu.main.view.ClearEditText;
import com.oneMap.module.common.global.ArouterConstants;
import com.oneMap.module.common.global.Global;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArouterConstants.TO_BAIDUACTIVITY)
public class BaiduActivity extends Activity implements OnGetGeoCoderResultListener, OnGetPoiSearchResultListener, View.OnClickListener {
    private String TAG = getClass().getSimpleName();

    private MapView mapView;
    /**
     * 默认城市名称
     */
    private EditText cityEt;
    /**
     * 请搜索您的小区或大厦、街道的名称
     */
    private ClearEditText searchEt;
    private ImageView locationIv;
    private ListView nearAddressList;
    private LinearLayout nearListEmptyLl;
    private LinearLayout nearAddressLl;
    private ListView searchAddressListView;
    /**
     * 没有搜索到相关结果\n请您选择用地图标注您所在的位置
     */
    private TextView searchEmptyTv;
    private LinearLayout searchLl;


    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BaiduMap mBaiduMap = null;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private boolean isFirstLoc = true;// 是否首次定位
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocClient;// 定位相关
    private PoiSearch mPoiSearch = null;
    private String cityName = "";

    private NearAddressAdapter nearAddressAdapter = null;
    private SearchAddressAdapter searchAddressAdapter = null;
    private List<PoiInfo> nearAddresses = new ArrayList<PoiInfo>();
    private List<PoiInfo> searchAddresses = new ArrayList<PoiInfo>();
    private ImageView iv_baidu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu);
        initView();
        cityEt.setText(Global.BAIDU_CITY_NAME);
        initMapsAndEvents();
    }

    protected void initMapsAndEvents() {

        // 定位图标点击，重新设置为初次定位
        locationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BaiduActivity.this, "正在定位中...", Toast.LENGTH_SHORT).show();
                isFirstLoc = true;
            }
        });
        cityName = Global.BAIDU_CITY_NAME;//这个可以通过定位获取
        // 隐藏比例尺和缩放图标
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                searchLl.setVisibility(View.GONE);
                mBaiduMap.clear();
                mBaiduMap.addOverlay(new MarkerOptions().position(arg0.target).icon(mCurrentMarker));
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0.target));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before,int count) {
                if (cs == null || cs.length() <= 0) {
                    searchLl.setVisibility(View.GONE);
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表
                 */
                String city = cityEt.getText().toString();
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(!"".equals(city) ? city : Global.BAIDU_CITY_NAME)
                        .keyword(cs.toString())
                        .pageNum(0)
                        .pageCapacity(30));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.marker_icon);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
        mLocClient = new LocationClient(this);
        // 设置地图缩放级别为15
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型 取值有3个： 返回国测局经纬度坐标系：gcj02
        // 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        option.setScanSpan(1000);// 扫描间隔 单位毫秒
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mLocClient.setLocOption(option);
        mLocClient.start();

        nearAddressAdapter = new NearAddressAdapter(this,R.layout.item_near_address, nearAddresses);
        nearAddressList.setAdapter(nearAddressAdapter);
        nearAddressList.setEmptyView(nearListEmptyLl);

        nearAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                PoiInfo poiInfo = nearAddresses.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("Lng", poiInfo.location.longitude + "");
                bundle.putString("Lat", poiInfo.location.latitude + "");
                bundle.putString("Address", poiInfo.name);
                bundle.putString("DetailedAddress", poiInfo.address);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchAddressAdapter = new SearchAddressAdapter(this,R.layout.item_search_address, searchAddresses);
        searchAddressListView.setAdapter(searchAddressAdapter);
        searchAddressListView.setEmptyView(searchEmptyTv);

        searchAddressListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        PoiInfo poiInfo = searchAddresses.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("Lng", poiInfo.location.longitude + "");
                        bundle.putString("Lat", poiInfo.location.latitude + "");
                        bundle.putString("Address", poiInfo.name);
                        bundle.putString("DetailedAddress", poiInfo.address);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mSearch.destroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView = null;
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduActivity.this, "抱歉，未能找到结果",Toast.LENGTH_LONG).show();
            return;
        }

        List<PoiInfo> list = result.getPoiList();
        if (list != null && list.size() > 0) {
            nearAddresses.clear();
            nearAddresses.addAll(list);
            nearAddressAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_baidu_back){
            finish();
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Address address = location.getAddress();
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            cityName = location.getCity();

//            if (isFirstLoc) {
//                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
//            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {

    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> list = result.getAllPoi();
            searchLl.setVisibility(View.VISIBLE);
            if (list != null && list.size() > 0) {
                searchAddresses.clear();
                searchAddresses.addAll(list);
                searchAddressAdapter.notifyDataSetChanged();
            }
        }
    }


    private void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        cityEt = (EditText) findViewById(R.id.city_et);
        searchEt = (ClearEditText) findViewById(R.id.search_et);
        locationIv = (ImageView) findViewById(R.id.location_iv);
        nearAddressList = (ListView) findViewById(R.id.near_address_list);
        nearListEmptyLl = (LinearLayout) findViewById(R.id.near_list_empty_ll);
        nearAddressLl = (LinearLayout) findViewById(R.id.near_address_ll);
        searchAddressListView = (ListView) findViewById(R.id.search_address_list_view);
        searchEmptyTv = (TextView) findViewById(R.id.search_empty_tv);
        searchLl = (LinearLayout) findViewById(R.id.search_ll);
        iv_baidu_back = (ImageView) findViewById(R.id.iv_baidu_back);
        iv_baidu_back.setOnClickListener(this);
    }
}
