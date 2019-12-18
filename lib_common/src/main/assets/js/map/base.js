var mapTool = {
    locationMarker: null,
    baiduLocationMarker: null,     //百度导航 点击后定位
    dmdzLocationMarker: null,     //地名地址 点击后定位
    //1.地图投影坐标系，从切片文件中获取;（墨卡托用）
    //2.例如：+proj=tmerc +lat_0=0 +lon_0=114 +k=1.000000 +x_0=500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs   （一般西安80/大地2000用）
    mapCoordinateSystem: "",
    //地图定位偏移值 mapOffsetX：横向水平偏移；mapOffsetX：纵向垂直偏移
    mapOffsetX:null,
    mapOffsetY:null,

    fangda: function () {
        map.zoomIn();
    },
    suoxiao: function () {
        map.zoomOut()
    },
    //地图复位
    fuwei: function () {
        map.setView(mapConfig.center, map.options.zoom);
    },
    //地名地址定位
    dmdzLocation:function(lat,lng){
       this.removeDmdzLocationMarker();
       var point = AIMap.latLng(lat, lng);
            this.dmdzLocationMarker = AIMap.marker(point, {
                icon: AIMap.icon({
                    iconUrl: 'images/measure_marker.png',
                    iconSize: [40, 40],
                    iconAnchor: [20, 20]
                })
            });
        map.addLayer(this.dmdzLocationMarker);
        map.setView(new AIMap.LatLng(lat, lng), map.getZoom());
    },
    //百度导航定位  显示在切片地图上
    baiduLocation: function (lng, lat) {
        this.mapCoordinateSystem = projStr;
        this.mapOffsetX = offsetX;
        this.mapOffsetY = offsetY;
        if (this.mapCoordinateSystem == "") {
            console.log("未定义投影坐标系");
            return;
        }
        this.removeLocationMarker();

        //百度坐标转换为国测火星坐标
        var gcj = bd09togcj02(lng, lat);
        //国测火星坐标转换为GPS坐标
        var wgs = gcj02towgs84(gcj[0], gcj[1]);

        //没有第二坐标系时，默认是WGS84坐标系。forward:转成定义的坐标系；inverse：由定义的坐标系转成WGS84
        var map_xy = proj4(this.mapCoordinateSystem, [wgs[0], wgs[1]]);
        var point = AIMap.latLng(map_xy[1] + this.mapOffsetY, map_xy[0] + this.mapOffsetX);
        this.baiduLocationMarker = AIMap.marker(point, {
            icon: AIMap.icon({
                iconUrl: 'images/dingwei.gif',
                iconSize: [60, 60],
                iconAnchor: [30, 40]
            })
        });

        //百度定位marker点添加点击事件
         this.baiduLocationMarker.on('click', function(e) {
         //回传百度坐标
          android.getBaiduLatlng(lng,lat);
         });

        map.addLayer(this.baiduLocationMarker);
        map.setView(point, map.getZoom());
    },
    //传入WGS84坐标，在影像地图上定位
    dingwei: function (lng, lat) {
        this.mapCoordinateSystem = projStr;
        this.mapOffsetX = offsetX;
        this.mapOffsetY = offsetY;
        if (this.mapCoordinateSystem == "") {
            console.log("未定义投影坐标系");
            return;
        }
        this.removeLocationMarker();
        //没有第二坐标系时，默认是WGS84坐标系。forward:转成定义的坐标系；inverse：由定义的坐标系转成WGS84
        var map_xy = proj4(this.mapCoordinateSystem, [lng, lat]);
//        var point = AIMap.latLng(map_xy[1] + this.mapOffsetX, map_xy[0] + this.mapOffsetY);
        var point = AIMap.latLng(map_xy[1] + this.mapOffsetY, map_xy[0] + this.mapOffsetX);
        this.locationMarker = AIMap.marker(point, {
            icon: AIMap.icon({
                iconUrl: 'images/marker_now.png',
                iconSize: [40, 40],
                iconAnchor: [20, 30]
            })
        });
        map.addLayer(this.locationMarker);
        map.setView(point, map.getZoom());
    },
    //传入影像地图坐标，转换成WGS84
    AimapToWGS84:function(lng, lat) {
        this.mapCoordinateSystem = projStr;
        if (this.mapCoordinateSystem == "") {
            console.log("未定义投影坐标系");
            return;
        }
        var wgs84_xy = proj4(this.mapCoordinateSystem).inverse([lng, lat]);
        return wgs84_xy;
    },
    //清除定位点
    removeLocationMarker: function () {
    //清除定位marker点
        if (this.locationMarker != null && map.hasLayer(this.locationMarker)) {
            map.removeLayer(this.locationMarker);
            this.locationMarker = null;
        }
    //清除百度导航定位marker点
        if (this.baiduLocationMarker != null && map.hasLayer(this.baiduLocationMarker)) {
            map.removeLayer(this.baiduLocationMarker);
            this.baiduLocationMarker = null;
        }

        //清除地名地址定位marker点
        this.removeDmdzLocationMarker();
    },
     //清除地名地址定位marker点
    removeDmdzLocationMarker: function () {
        if (this.dmdzLocationMarker != null && map.hasLayer(this.dmdzLocationMarker)) {
            map.removeLayer(this.dmdzLocationMarker);
            this.dmdzLocationMarker = null;
        }
    },
    ceju: function (flag) {
        map.off("click", mapClick);
        map.off("dragend", mapDragEnd);
        switch (flag) {
            case 'zhunxin':
                map.off("click", getdistance);
                break;
            case 'shouhui':
                map.off("click", getdistance);
                map.on("click", getdistance);
                break;
        }
    },
    huamian: function (flag) {
        map.off("click", mapClick);
        map.off("dragend", mapDragEnd);
        switch (flag) {
            case 'zhunxin':
                map.off("click", getArea);
                break;
            case 'shouhui':
                map.off("click", getArea);
                map.on("click", getArea);
                break;
        }
    }
}
var pointArray = [];//存放坐标点
var markerArray = [];//存放坐标点marker
var polylineArray = [];//存放每条线
var polygon = null;//存放画的面
//测量过程中准心
function getAccordinate(flag) {
    var mapContainer = $("#map");
    var obj = { latlng: map.containerPointToLatLng([mapContainer.width() / 2, mapContainer.height() / 2]) };
    if (flag == "ceju") {
        getdistance(obj);
    }
    else if (flag == "huamian") {
        getArea(obj);
    }
}
//测量过程中撤销
function Draw_backout(flag) {
    //移除最后一次添加的点
    if (markerArray.length > 0) {
        map.removeLayer(markerArray.pop());
    }
    //重新绘制多边形
    if (pointArray.length == 1) {
        pointArray = [];
        android.bzMesureResult(flag, "0");
        return;
    }
    if (flag == "ceju") {
        if (pointArray.length >= 2) {
            if (polylineArray.length > 0) {
                if (map.hasLayer(polylineArray[polylineArray.length - 1])) {
                    map.removeLayer(polylineArray[polylineArray.length - 1]);
                }
                polylineArray.pop();
            }
            pointArray.pop();
            android.bzMesureResult(flag, computeDistance());
            return;
        }
    }
    else if (flag == "huamian") {
        if (pointArray.length == 2) {
            if (polylineArray.length > 0) {
                if (polylineArray[0] != null && map.hasLayer(polylineArray[0])) {
                    map.removeLayer(polylineArray[0]);
                    polylineArray = [];
                }
            }
            pointArray.pop();
            android.bzMesureResult(flag, "");
            return;
        }
        if (pointArray.length == 3) {
            pointArray.pop();
            if (map.hasLayer(polygon)) {
                map.removeLayer(polygon);
            }
            var newAddPolyline = new AIMap.Polyline([pointArray[0], pointArray[1]], { color: 'red' });
            newAddPolyline.addTo(map);
            polylineArray.push(newAddPolyline);
            android.bzMesureResult(flag, computeDistance());
            return;
        }
        if (pointArray.length > 3) {
            pointArray.pop();
            var _pointArray = pointArray;
            if (map.hasLayer(polygon)) {
                map.removeLayer(polygon);
            }
            polygon = new AIMap.Polygon(_pointArray, { color: 'red', opacity: 0.5 });
            polygon.addTo(map);
            var area = computeArea(pointArray[pointArray.length -1]);
            var pf = (area > 1000000 ? (area / 1000000).toFixed(2) + '平方千米' : Math.ceil(area) + '平方米');
            var mu = ((Number(area) * 0.0015).toFixed(2)) + "亩";
            var gq = ((Number(area) * 0.0001).toFixed(2)) + "公顷";
            android.bzMesureResult("huamian", computeDistance() + " " + pf + "  " + mu + "  " + gq);
        }
    }
}
//测量过程中手绘距离
function getdistance(e) {
    pointArray.push(e.latlng);
    var newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point" + ".png'/></div>" }) });
    newAddMarker.addTo(map);
    markerArray.push(newAddMarker);
    if (pointArray.length > 1) {
        var newAddPolyline = new AIMap.Polyline([e.latlng, pointArray[pointArray.length - 2]], { color: 'red', weight: 5 });
        newAddPolyline.addTo(map);
        polylineArray.push(newAddPolyline);
        android.bzMesureResult("ceju", computeDistance());
    }
}
//测量过程中手绘画面
function getArea(e) {
    pointArray.push(e.latlng);
    var newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point" + ".png'/></div>" }) });
    newAddMarker.addTo(map);
    markerArray.push(newAddMarker);
    if (pointArray.length == 2) {
        var newAddPolyline = new AIMap.Polyline([e.latlng, pointArray[pointArray.length - 2]], { color: 'red', weight: 5 });
        newAddPolyline.addTo(map);
        polylineArray.push(newAddPolyline);
        android.bzMesureResult(flag, computeDistance());
    }
    if (pointArray.length > 2) {
        //先把线清空
        for (var i = 0; i < polylineArray.length; i++) {
            if (map.hasLayer(polylineArray[i])) {
                map.removeLayer(polylineArray[i]);
            }
        }
        polylineArray = [];

        //画面
        if (polygon != null && map.hasLayer(polygon)) {
            map.removeLayer(polygon);
            polygon = null;
        }
        polygon = new AIMap.Polygon(pointArray, { color: 'red', opacity: 0.5 });
        polygon.addTo(map);

        //计算面积和周长
        var area = computeArea(e.latlng);
        var pf = (area > 1000000 ? (area / 1000000).toFixed(2) + '平方千米' : Math.ceil(area) + '平方米');
        var mu = ((Number(area) * 0.0015).toFixed(2)) + "亩";
        var gq = ((Number(area) * 0.0001).toFixed(2)) + "公顷";
        android.bzMesureResult("huamian", computeDistance() + " " + pf + " " + mu + " " + gq);
    }
    if (pointArray.length > 2) {
        //重绘第一个点，注明最后一段距离长度
        if (markerArray[0] != null && map.hasLayer(markerArray[0])) {
            map.removeLayer(markerArray[0]);
            var  newAddMarker = new AIMap.Marker(pointArray[0], { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point.png'/></div>" }) })
            markerArray[0] = newAddMarker;
            map.addLayer(newAddMarker);
        }
    }
}
//清除测量结果
function measureClear() {
    pointArray = [];
    for (var i = 0; i < polylineArray.length; i++) {
        if (map.hasLayer(polylineArray[i])) {
            map.removeLayer(polylineArray[i]);
        }
    }
    polylineArray = [];
    for (var i = 0; i < markerArray.length; i++) {
        if (map.hasLayer(markerArray[i])) {
            map.removeLayer(markerArray[i]);
        }
    }
    markerArray = [];
    if (polygon != null && map.hasLayer(polygon)) {
        map.removeLayer(polygon);
        polygon = null;
    }
}
//结束测量
function measureEnd() {
    measureClear();
    map.off("click", getdistance);
    map.off("click", getArea);
    map.on("click", mapClick);
    map.on("dragend", mapDragEnd);
}
//计算距离
function computeDistance() {
    var len = pointArray.length;
    var distance_sum = 0;
    for (var k = 0; k < len - 1; k++) {
        var _distance = 0;
        if (pointArray[k].lat > 180 || pointArray[k].lat < -180) {
            _distance = pointArray[k].distance80To(pointArray[k + 1]);
        } else {
            _distance = pointArray[k].distanceTo(pointArray[k + 1]);
        }
        distance_sum += _distance;
    }
    var distanceStr = distance_sum > 1000 ? (distance_sum / 1000).toFixed(2) + '千米' : Math.ceil(distance_sum) + '米';
    return distanceStr;
}
//计算面积
function computeArea(latlng) {
    var sum = 0.0;
    var len = pointArray.length;
    if (latlng.lat > 180 || latlng.lat < -180) {
        sum += pointArray[0].lat * (pointArray[len - 1].lng - pointArray[1].lng);
    }else {
        sum += pointArray[0].lat * (pointArray[len - 1].lng - pointArray[1].lng) * 111194.872221777 * 111194.872221777;
    }
    for (var k = 1; k < len; k++) {
        if (latlng.lat > 180 || latlng.lat < -180) {
            sum += pointArray[k].lat * (pointArray[k - 1].lng - pointArray[(k + 1) % len].lng);
        }else {
            sum += pointArray[k].lat * (pointArray[k - 1].lng - pointArray[(k + 1) % len].lng) * 111194.872221777 * 111194.872221777;
        }
    }
    return Math.abs(-sum / 2.0);
}


//返回坐标值（距离量算模块和采集模块使用）  在原有基础上修改 20190813
function measureSave(flag,moduleType) {
    var accordinates = "";
    var pointLength = pointArray.length;
    if (pointLength == 2) {
        for (var i = 0; i < pointLength; i++) {
            accordinates += pointArray[i].lng + " " + pointArray[i].lat + ",";
        }
        if (flag == "huamian") {
            if(moduleType == "measure") {
               android.bzRecordCoordinates("", flag);
            }else{
               android.gatherCoordinates("", moduleType);
            }
        }
        else {
            accordinates = accordinates.substr(0, accordinates.length - 1);
            android.bzRecordCoordinates(accordinates, flag);
        }

    }
    else if(pointLength > 2){
        for (var i = 0; i < pointLength; i++) {
            accordinates += pointArray[i].lng + " " + pointArray[i].lat + ",";
        }
        if (flag == "huamian") {
            accordinates += pointArray[0].lng + " " + pointArray[0].lat;
        }
        else {
            accordinates = accordinates.substr(0, accordinates.length - 1);
        }
         if(moduleType == "measure") {
           android.bzRecordCoordinates(accordinates, flag);
         }else{
           android.gatherCoordinates(accordinates, moduleType);
         }
    }
    else {
         if(moduleType == "measure") {
           android.bzRecordCoordinates(accordinates, flag);
         }else{
           android.gatherCoordinates(accordinates, moduleType);
         }
    }
}





function gatherMeasureBack(picsize){
       android.gatherMeasureBackLayout(picsize);
}








//function measureSave(flag) {
//    var accordinates = "";
//    var pointLength = pointArray.length;
//    if (pointLength == 2) {
//        for (var i = 0; i < pointLength; i++) {
//            accordinates += pointArray[i].lng + " " + pointArray[i].lat + ",";
//        }
//        if (flag == "humian") {
//            android.bzRecordCoordinates("", flag);
//        }
//        else {
//            accordinates = accordinates.substr(0, accordinates.length - 1);
//            android.bzRecordCoordinates(accordinates, flag);
//        }
//
//    }
//    else if(pointLength > 2){
//        for (var i = 0; i < pointLength; i++) {
//            accordinates += pointArray[i].lng + " " + pointArray[i].lat + ",";
//        }
//        if (flag == "humian") {
//            accordinates += pointArray[0].lng + " " + pointArray[0].lat;
//        }
//        else {
//            accordinates = accordinates.substr(0, accordinates.length - 1);
//        }
//        android.bzRecordCoordinates(accordinates, flag);
//    }
//    else {
//        android.bzRecordCoordinates("", flag);
//    }
//}