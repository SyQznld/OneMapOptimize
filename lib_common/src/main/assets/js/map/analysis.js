﻿﻿var analysis_pointArray = [];//存放坐标点
var analysis_markerArray = [];//存放坐标点marker
var analysis_polylineArray = [];//存放每条线
var analysis_polygon = null;//存放画的面
var analysis_polygonMarker = null;
var curr_zx_polygons=[];//存放当前加载的数据



function analysis_measureEnd() {
     var accordinates = "";
     if(analysis_pointArray.length >2){
     for (var i = 0; i < analysis_pointArray.length; i++) {
         accordinates += analysis_pointArray[i].lng + " " + analysis_pointArray[i].lat + ", ";
     }
     if (accordinates != "") {
         accordinates += analysis_pointArray[0].lng + " " + analysis_pointArray[0].lat;
         accordinates = "POLYGON ((" + accordinates + "))";
     }else{
         accordinates = accordinates.substr(0,accordinates.length - 1);
     }

        android.getAnalysisi_Area(accordinates);
    }else{
        android.getAnalysisi_Area("");
    }

        map.off("click",analysis_mapClickPoint);
        map.off("click",analysis_Draw_backout);
        map.off("dragend", mapDragEnd);

}



function analysis_measureBack() {
    analysis_measureClear();

    map.off("click",analysis_mapClickPoint);
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);
    map.on("click",mapClick);
    map.on("dragend", mapDragEnd);
}

function analysis_measure_area() {
    analysis_measureClear();
    $(".measure-accordinate").show();
    map.off("click", analysis_mapClickPoint);
    map.on("click", analysis_mapClickPoint);
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);
}
function analysis_measureClear() {
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);


    analysis_pointArray = [];
    for (var i = 0; i < analysis_polylineArray.length; i++) {
        if (map.hasLayer(analysis_polylineArray[i])) {
            map.removeLayer(analysis_polylineArray[i]);
        }
    }
    analysis_polylineArray = [];
    for (var i = 0; i < analysis_markerArray.length; i++) {
        if (map.hasLayer(analysis_markerArray[i])) {
            map.removeLayer(analysis_markerArray[i]);
        }
    }
    analysis_markerArray = [];
    if (analysis_polygon != null && map.hasLayer(analysis_polygon)) {
        map.removeLayer(analysis_polygon);
        analysis_polygon = null;
    }
    if (analysis_polygonMarker != null && map.hasLayer(analysis_polygonMarker)) {
        map.removeLayer(analysis_polygonMarker);
        analysis_polygonMarker = null;
    }
}


function analysis_getAccordinate() {
        var mapContainer = $("#map");
        var obj = { latlng: map.containerPointToLatLng([mapContainer.width() / 2, mapContainer.height() / 2]) };
        analysis_mapClickPoint(obj);
}
function analysis_mapClickPoint(e) {
        analysis_pointArray.push(e.latlng);
        var newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point" + (analysis_pointArray.length == 1 ? "1" : "") + ".png'/></div>" }) });
        if (analysis_pointArray.length > 1) {
            newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point" + (analysis_pointArray.length == 1 ? "1" : "") + ".png'/></div>" }) });
        }
        if (analysis_pointArray.length == 2) {
            var newAddPolyline = new AIMap.Polyline([e.latlng, analysis_pointArray[analysis_pointArray.length - 2]], { color: 'red' });
            newAddPolyline.addTo(map);
            analysis_polylineArray.push(newAddPolyline);
        }
        if (analysis_pointArray.length > 2) {
            //先把线清空
            for (var i = 0; i < analysis_polylineArray.length; i++) {
                if (map.hasLayer(analysis_polylineArray[i])) {
                    map.removeLayer(analysis_polylineArray[i]);
                }
            }
            analysis_polylineArray = [];

            //画面
            if (analysis_polygon != null && map.hasLayer(analysis_polygon)) {
                map.removeLayer(analysis_polygon);
                analysis_polygon = null;
            }
            analysis_polygon = new AIMap.Polygon(analysis_pointArray, { color: 'red', fill:false});
            analysis_polygon.addTo(map);
        }
        newAddMarker.addTo(map);
        analysis_markerArray.push(newAddMarker);

        if (analysis_pointArray.length > 2) {
            //重绘第一个点，注明最后一段距离长度
            if (analysis_markerArray[0] != null && map.hasLayer(analysis_markerArray[0])) {
                map.removeLayer(analysis_markerArray[0]);

                newAddMarker = new AIMap.Marker(analysis_pointArray[0], { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_point.png'/></div>" }) })
                analysis_markerArray[0] = newAddMarker;
                map.addLayer(newAddMarker);
            }
        }

}

//画面 撤销一步
function analysis_Draw_backout() {
    //移除最后一次添加的点
    if (analysis_markerArray.length > 0) {
        map.removeLayer(analysis_markerArray.pop());
    }
    //重新绘制多边形
    if(analysis_pointArray.length == 1){
        analysis_pointArray = [];
    }
    if (analysis_pointArray.length == 2) {
        if (analysis_polylineArray.length > 0) {
            if (analysis_polylineArray[0] != null && map.hasLayer(analysis_polylineArray[0])) {
                map.removeLayer(analysis_polylineArray[0]);
                analysis_polylineArray=[];
            }
        }
        analysis_pointArray.pop();
    }
    if (analysis_pointArray.length ==3) {
       analysis_pointArray.pop();
       if (map.hasLayer(analysis_polygon)) {
           map.removeLayer(analysis_polygon);
       }
       var newAddPolyline = new AIMap.Polyline([analysis_pointArray[0], analysis_pointArray[1]], { color: 'red' });
           newAddPolyline.addTo(map);
           analysis_polylineArray.push(newAddPolyline);
    }
    if(analysis_pointArray.length >3){
            analysis_pointArray.pop();
            var _pointArray = analysis_pointArray;
            if (map.hasLayer(analysis_polygon)) {
                map.removeLayer(analysis_polygon);
            }
            analysis_polygon = new AIMap.Polygon(_pointArray, { color: 'red', opacity: 0.5 });
            analysis_polygon.addTo(map);
    }
}




//显示所有图斑信息  智能分析
var allQueryResultPolygons  = [];
function loadQueryResultData(data1) {
    clearQueryPolygons();
    var data = eval("(" + data1 + ")");
    if (data != null && data.Rows != null && data.Rows.length > 0) {
        var feature_length = data.Rows.length;
        var suijise = "blue";
        var cur_function_polys = [];
        var cur_function_polys_labels = [];
        for (var i = 0; i < feature_length; i++) {
            var _ll = [];

            var _geom = data.Rows[i].ogr_geometry;
            console.log(_geom);
            if (_geom.indexOf("MULTIPOLYGON ") != -1) {
                        _geom = _geom.replace("MULTIPOLYGON (((", "");
                        _geom = _geom.replace(")))", "");
                        _t = _geom.split(")), ((");
                        for (var m = 0; m < _t.length; m++) {
                            if (_t[m].indexOf("), (") != -1) {
                                var _ll1 = [];
                                var _t1 = _t[m].split("), (");
                                for (var p = 0; p < _t1.length; p++) {
                                    var _ll2 = [];
                                    var _t2 = _t1[p].split(", ");
                                    for (var n = 0; n < _t2.length; n++) {
                                        var _t3 = _t2[n].split(" ");
                                        _ll2.push([_t3[1], _t3[0]]);
                                    }
                                    _ll1.push(_ll2);
                                }
                                _ll.push(_ll1);
                            }
                            else {
                                var _ll1 = [];
                                var _t1 = _t[m].split(", ");
                                for (var n = 0; n < _t1.length; n++) {
                                    var _t2 = _t1[n].split(" ");
                                    _ll1.push([_t2[1], _t2[0]]);
                                }
                                _ll.push(_ll1);
                            }
                        }
                    } else {
                        _geom = _geom.replace("POLYGON ((", "");
                        _geom = _geom.replace("))", "");
                        _t = _geom.split("), (");
                        for (var ii = 0; ii < _t.length; ii++) {
                            var _ll1 = [];
                            var _t1 = _t[ii].split(", ");
                            for (var ij = 0; ij < _t1.length; ij++) {
                                var _t2 = _t1[ij].split(" ");
                                _ll1.push([_t2[1], _t2[0]]);
                            }
                            _ll.push(_ll1);
                        }
                    }

            var pg = new AIMap.Polygon(_ll, { weight: 2, fill: false, color: suijise, clickable: false });
            map.addLayer(pg);
            allQueryResultPolygons.push(pg);
        }
    }
}





function clearQueryPolygons() {
    if (allQueryResultPolygons.length > 0) {
        for (var i = 0; i < allQueryResultPolygons.length; i++) {
            if (allQueryResultPolygons[i] && map.hasLayer(allQueryResultPolygons[i])) {
                map.removeLayer(allQueryResultPolygons[i]);
            }
        }
        allQueryResultPolygons = [];
    }
}







//显示单个图斑
var dingweiPolygon;
function showFW(accordinate){
console.log("==========" + accordinate);
  analysis_clearLocationPolygon();
if (accordinate != "" && accordinate.length > 0) {
    var _ll = [];
    var _geom = accordinate;
    var _t;
    if (_geom.indexOf("MULTIPOLYGON ") != -1) {
        _geom = _geom.replace("MULTIPOLYGON (((", "");
        _geom = _geom.replace(")))", "");
        _t = _geom.split(")), ((");
    } else {
        _geom = _geom.replace("POLYGON ((", "");
        _geom = _geom.replace("))", "");
        _t = _geom.split("), (");
    }
    for (var ii = 0; ii < _t.length; ii++) {
        var _ll1 = [];
        var _t1 = _t[ii].split(", ");
        for (var ij = 0; ij < _t1.length; ij++) {
            var _t2 = _t1[ij].split(" ");
            _ll1.push([_t2[1], _t2[0]]);
        }
        _ll.push(_ll1);
    }
    if (_t.length == 1) {
        _ll = _ll[0];
    }
     dingweiPolygon = new AIMap.Polygon(_ll, { color: 'red', weight: 4, opacity: 1, fill: false, clickable: false });
     map.addLayer(dingweiPolygon);
     map.setView(dingweiPolygon.getBounds().getCenter(), map.getBoundsZoom(dingweiPolygon.getBounds()));
}
}

function analysis_clearLocationPolygon(){
 if (map.hasLayer(dingweiPolygon)) {
      map.removeLayer(dingweiPolygon);
    }
}



//显示在线图层图斑
var zx_dingweiPolygon;
function zx_showTuBan(accordinate){
//  var cur_function_polys = [];
//  zx_clearLocationPolygon();
if (accordinate != "" && accordinate.length > 0) {
    var _ll = [];
    var _geom = accordinate;
    var _t;
    if (_geom.indexOf("MULTIPOLYGON ") != -1) {
        _geom = _geom.replace("MULTIPOLYGON (((", "");
        _geom = _geom.replace(")))", "");
        _t = _geom.split(")), ((");
    } else {
        _geom = _geom.replace("POLYGON ((", "");
        _geom = _geom.replace("))", "");
        _t = _geom.split("), (");
    }
    for (var ii = 0; ii < _t.length; ii++) {
        var _ll1 = [];
        var _t1 = _t[ii].split(", ");
        for (var ij = 0; ij < _t1.length; ij++) {
            var _t2 = _t1[ij].split(" ");
            _ll1.push([_t2[1], _t2[0]]);
        }
        _ll.push(_ll1);
    }
    if (_t.length == 1) {
        _ll = _ll[0];
    }

     zx_dingweiPolygon = new AIMap.Polygon(_ll, { weight: 4, fillOpacity: 0.2,fillColor: "yellow", color: "blue", clickable: true });
     map.addLayer(zx_dingweiPolygon);
     map.setView(zx_dingweiPolygon.getBounds().getCenter(), map.getBoundsZoom(zx_dingweiPolygon.getBounds()));

 }
}




function zx_clearLocationPolygon(){
 if (map.hasLayer(zx_dingweiPolygon)) {
      map.removeLayer(zx_dingweiPolygon);
    }
}




//显示所有图斑信息  在线图层
function zx_loadQueryResultData(data1,name) {
    var data = eval("(" + data1 + ")");
var zx_allQueryResultPolygons  = [];
    var suijise = "blue";
    var cur_function_polys = [];
    var cur_function_polys_labels = [];
    if (data != null && data.length != null && data.length > 0) {
        var data_length=data.length;
        for(var j=0;j<data_length;j++){
            //二级结构，嵌套数组
          var shp_length=data[j].shp.length;
          for(var m=0;m<shp_length;m++ ){
          var _ll = [];
          var ogr_fid = data[j].shp[m].ogr_fid;
          var _geom = data[j].shp[m].ogr_geometry;
          var zxtc_type = data[j].type;
//          var _geom = data[j].ogr_geometry;
//          var ogr_fid = data[j].ogr_fid;
//          var zxtc_type = zxtc_type1;
          var _t;
          if (_geom.indexOf("MULTIPOLYGON ") != -1) {
              _geom = _geom.replace("MULTIPOLYGON (((", "");
              _geom = _geom.replace(")))", "");
              _t = _geom.split(")), ((");
          } else {
              _geom = _geom.replace("POLYGON ((", "");
              _geom = _geom.replace("))", "");
              _t = _geom.split("), (");
          }
          for (var ii = 0; ii < _t.length; ii++) {
              var _ll1 = [];
              var _t1 = _t[ii].split(", ");
              for (var ij = 0; ij < _t1.length; ij++) {
                  var _t2 = _t1[ij].split(" ");
                  _ll1.push([_t2[1], _t2[0]]);
              }
              _ll.push(_ll1);
          }
          if (_t.length == 1) {
              _ll = _ll[0];
          }
          var pg = new AIMap.Polygon(_ll, {
          zxtc_type:zxtc_type,
          ogr_geometry:_geom,
          ogr_fid:ogr_fid,
          weight: 2, fillOpacity: 0.1,fillColor: "yellow", color: "blue", clickable: true });
          pg.on('click', pgClick);
          map.addLayer(pg);
          zx_allQueryResultPolygons.push(pg);
          }
               var _layer = { layer: zx_allQueryResultPolygons, name: name };
               curr_zx_polygons.push(_layer);
        }
    }
}

function zx_clearQueryPolygons(name) {
    if (curr_zx_polygons.length > 0) {
        for (var i = 0; i < curr_zx_polygons.length; i++) {
            if (curr_zx_polygons[i].name==name) {
                for(var j=0;j<curr_zx_polygons[i].layer.length;j++){
                    map.removeLayer(curr_zx_polygons[i].layer[j]);
                }
                curr_zx_polygons.splice(i,1);
            }
        }
    }
}


//在线图层 图斑点击事件
function pgClick(e) {
    if (e != "") {
        var ogr_fid = e.target.options.ogr_fid;
        var ogr_geometry = e.target.options.ogr_geometry;
        var zxtc_type = e.target.options.zxtc_type;
        locationPolygon(ogr_fid, ogr_geometry,zxtc_type);
    }
}

 var selectedPolygon = null;
function locationPolygon(ogr_fid, ogr_geometry,zxtc_type){
         zxtb_removeLocation();

        var _geom = ogr_geometry;
        var _ll = [];
        var _t;
        if (_geom.indexOf("MULTIPOLYGON ") != -1) {
            _geom = _geom.replace("MULTIPOLYGON (((", "");
            _geom = _geom.replace(")))", "");
            _t = _geom.split(")), ((");
        } else {
            _geom = _geom.replace("POLYGON ((", "");
            _geom = _geom.replace("))", "");
            _t = _geom.split("), (");
        }
        for (var ii = 0; ii < _t.length; ii++) {
            var _ll1 = [];
            var _t1 = _t[ii].split(", ");
            for (var ij = 0; ij < _t1.length; ij++) {
                var _t2 = _t1[ij].split(" ");
                _ll1.push([_t2[1], _t2[0]]);
            }
            _ll.push(_ll1);
        }
        if (_t.length == 1) {
            _ll = _ll[0];
        }

        selectedPolygon = new AIMap.Polygon(_ll, {weight: 3, fillOpacity: 0.1, fillColor: "#40E0D0", color: "yellow", clickable: true });
        selectedPolygon.on('click', pgClick);
        map.addLayer(selectedPolygon);
//          map.setView(selectedPolygon.getBounds().getCenter(),map.getMaxZoom());
        map.setView(selectedPolygon.getBounds().getCenter(),3);
        android.checkTbDetail(ogr_fid, ogr_geometry,zxtc_type);
    }



 function zxtb_removeLocation() {
     if (selectedPolygon && map.hasLayer(selectedPolygon)) {
         map.removeLayer(selectedPolygon);
     }
 }
 //显示图斑中每张图片角度
 //../upload/tb_xianzhuang/2019-03-28-27/70aff57682d845128264c62deb9c7897&-45.58&34.870915,113.636969.jpg
 var arrowMarker = null;
  function showTb_picAngle(photo_path,photo_xy,photo_angle) {
         clearArrowMarker();
         //图片拍摄角度
         var _jiaodu = photo_angle;
         _jiaodu = parseFloat(_jiaodu);
         var fangwei = "";
         if (_jiaodu >= -15 && _jiaodu < 15) {
             fangwei = "正北方向";
         }
         else if (_jiaodu >= 15 && _jiaodu < 75) {
             fangwei = "东北方向";
         }
         else if (_jiaodu >= 75 && _jiaodu <= 105) {
             fangwei = "正东方向";
         }
         else if (_jiaodu >= 105 && _jiaodu < 165) {
             fangwei = "东南方向";
         }
         else if ((_jiaodu >= 165 && _jiaodu <= 180) || (_jiaodu) >= -180 && _jiaodu < -165) {
             fangwei = "正南方向";
         }
         else if (_jiaodu >= -165 && _jiaodu < -100) {
             fangwei = "西南方向";
         }
         else if (_jiaodu >= -165 && _jiaodu < -65) {
             fangwei = "正西方向";
         }
         else if (_jiaodu >= -65 && _jiaodu < -15) {
             fangwei = "西北方向";
         }

         var pname = photo_path.split("/")[3];
         var picAndmarkerID = pname.split("&")[0];
         $("#imgs").append('<div class="col-md-2 photoWidth">' +
             '<div class="thumbnail"><img src="../' + photo_path + '" alt="' + pname.substr(0, pname.length - 4) + '"><h5 style="text-align: center;" id=' + picAndmarkerID + '>' + fangwei + '</h5></div></div>');

         //图片位置图标
         var _p = photo_xy.split(",");
           //         466860.1134535605,3860310.566421133       直接在切片上点击
           //         3105887.2870444874,10538662.396374162     谷歌地球上点击当前位置的点转换后
           var _pReal = transformPoint(parseFloat(_p[1]), parseFloat(_p[0]));   //466860.1134535605,3860310.566421133
           var point = AIMap.latLng(_pReal[1], _pReal[0]);
           arrowMarker = AIMap.marker(point, {
                     icon: AIMap.divIcon({
                    html:"<div id='" + picAndmarkerID + "MapArrow'><img src='images/arrow01.png' width=30 height=50 /></div>",
                 })
             });
         map.addLayer(arrowMarker);
       $("#" + picAndmarkerID + "MapArrow").rotate({ center: ["10px", "100%"], angle: _jiaodu });
  }


  //清除显示角度的箭头
function clearArrowMarker(){
      if (arrowMarker && map.hasLayer(arrowMarker)) {
              map.removeLayer(arrowMarker);
          }
}

function transformPoint(y, x) {
         return proj4('PROJCS["China_2000_3_Degree_GK_CM_114E",GEOGCS["GCS_china_2000",DATUM["D_china_2000",SPHEROID["china_2000",6378137.0,298.257222101]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]],PROJECTION["Transverse_Mercator"],PARAMETER["false_easting",500000.0],PARAMETER["false_northing",0.0],PARAMETER["central_meridian",114.0],PARAMETER["scale_factor",1.0],PARAMETER["latitude_of_origin",0.0],UNIT["Meter",1.0]]', [x, y]);
}






