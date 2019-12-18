//图层加载
var layerGroup = [],
    locationPolygon = null;
var createLayers = {
    "raster": function (data) {
        if (data.isShow) {
            var yx = new AIMap.TileLayer.ArcGIS(mapdataPath + getMapdataUrl(data.name), {
                minZoom: 0,
                maxZoom: map.getMaxZoom(),
                format: 'png',
                transparent: true,
                opacity: 1.0,
                tileSize: 256,
                tileOrigin: mapConfig.origin,
                resolutions: mapConfig.crs.options.resolutions,
                maxExtent: mapConfig.crs.options.bounds
            });
            map.addLayer(yx);

            var layerMapCenter = data.layerViewCenter;
//          //判断 如果需要改变中心点，则改变视野
//            if(layerMapCenter != "" && layerMapCenter != undefined){
//              if(layerMapCenter.match(",")){
//                 var ss = layerMapCenter.split(",");
//                 var layerCenter = [ss[1],ss[0]];
//                 map.setView(layerCenter,map.getZoom());
//              }
//            }else{
//                 map.setView(mapConfig.center,map.getZoom());
//            }


            layerGroup.push({
                type: 'raster',
                name: data.name,
                layer: yx,
                identify: data.identify,
                title: data.title,
                layerViewCenter: data.layerViewCenter,
                path: data.path
            });

           if(data.identify){
             res = "";
             loadAjax(layerGroup[layerGroup.length - 1]);
           }
        } else {
            removeLayer(data.name);
            removeLocation();
        }
    },
    "vector": function (data) {
        createShpLayer(data);
    }
};
//图层加载调用接口，type:raster/Vector,data:其它option
var loadLayers = function (type, data) {
    return createLayers[type](data);
};
function createShpLayer(obj) {
    if (obj.isShow) {
        $.ajax({
            url: obj.path + obj.name + ".txt",
            dataType: "json",
            async: false,
            success: function (data) {
            console.log("data1111111"+data);
                if (data != null && data.features.length > 0) {
                    var cur_function_polys = [];
                    var feature_length = data.features.length;
                    switch (data.features[0].geometry.type) {
                        case 'Point':
                        case 'MultiPoint':
                                for (var i = 0; i < feature_length; i++) {
                                    if (data.features[i].geometry != null) {
                                        var zuobiaoArray = data.features[i].geometry.coordinates;
                                        var marker = null;
                                        for (var ii = 0; ii < zuobiaoArray.length; ii++) {
                                            var _t1 = zuobiaoArray[ii];
                                            if (_t1 instanceof (Array)) {
                                                marker = AIMap.marker(new AIMap.LatLng(_t1[1], _t1[0], true), {
                                                    icon: AIMap.icon({
                                                        iconUrl: 'images/marker_now.png',
                                                        iconSize: [40, 40],
                                                        iconAnchor: [20, 30]
                                                    })
                                                });
                                                cur_function_polys.push(marker);
                                            }
                                            else{
                                                marker = AIMap.marker(new AIMap.LatLng(zuobiaoArray[1], zuobiaoArray[0], true), {
                                                    icon: AIMap.icon({
                                                        iconUrl: 'images/marker_now.png',
                                                        iconSize: [40, 40],
                                                        iconAnchor: [20, 30]
                                                    })
                                                });
                                                cur_function_polys.push(marker);
                                                break;
                                            }
                                        }
                                    }
                                }
                            break;
                        case 'LineString':
                        case 'MultiLineString':
                            for (var i = 0; i < feature_length; i++) {
                                if (data.features[i].geometry != null) {
                                    var zuobiaoArray = data.features[i].geometry.coordinates;
                                    var lll = [];
                                    for (var ii = 0; ii < zuobiaoArray.length; ii++) {
                                        var _ll1 = [];
                                        var _t1 = zuobiaoArray[ii];
                                        if (_t1[0] instanceof (Array)) {
                                            for (var ii2 = 0; ii2 < _t1.length; ii2++) {
                                                var _ll12 = [];
                                                var _t12 = _t1[ii2];
                                                if (_t12.length > 0 && _t12[0] instanceof (Array)) {
                                                    for (var ij2 = 0; ij2 < _t12.length; ij2++) {
                                                        var _t22 = _t12[ij2];
                                                        var p2 = new AIMap.LatLng(_t22[1], _t22[0], true);
                                                        _ll12.push(p2);
                                                    }
                                                    _ll1.push(_ll12);
                                                } else {
                                                    var p2 = new AIMap.LatLng(_t12[1], _t12[0], true);
                                                    _ll1.push(p2);
                                                }
                                            }
                                        }
                                        else {
                                            var p2 = new AIMap.LatLng(_t1[1], _t1[0], true);
                                            _ll1.push(p2);
                                        }
                                        lll.push(_ll1);
                                    }

                                    if (zuobiaoArray.length == 1) {
                                        lll = lll[0];
                                    }
                                    var pg = new AIMap.polyline(lll, {
                                        weight: 3,
                                        fillOpacity: 0.5,
                                        fillColor: (obj.color != "" && obj.color != undefined) ? obj.color : "red",
                                        color: (obj.color != "" && obj.color != undefined) ? obj.color : "red",
                                    });
                                    cur_function_polys.push(pg);
                                }
                            }
                            break;
                        case 'Polygon':
                        case 'MultiPolygon':
                            for (var i = 0; i < feature_length; i++) {
                                if (data.features[i].geometry != null) {
                                    var zuobiaoArray = data.features[i].geometry.coordinates;
                                    var lll = [];
                                    for (var ii = 0; ii < zuobiaoArray.length; ii++) {
                                        var _ll1 = [];
                                        var _t1 = zuobiaoArray[ii];
                                        for (var ii2 = 0; ii2 < _t1.length; ii2++) {
                                            var _ll12 = [];
                                            var _t12 = _t1[ii2];
                                            if (_t12.length > 0 && _t12[0] instanceof (Array)) {
                                                for (var ij2 = 0; ij2 < _t12.length; ij2++) {
                                                    var _t22 = _t12[ij2];
                                                    var p2 = new AIMap.LatLng(_t22[1], _t22[0], true);
                                                    _ll12.push(p2);
                                                }
                                                _ll1.push(_ll12);
                                            } else {
                                                var p2 = new AIMap.LatLng(_t12[1], _t12[0], true);
                                                _ll1.push(p2);
                                            }
                                        }
                                        lll.push(_ll1);
                                    }
                                    if (zuobiaoArray.length == 1) {
                                        lll = lll[0];
                                    }
                                    var pg = new AIMap.Polygon(lll, {
                                        weight: 2,
                                        fillOpacity: 0.2,
                                        fillColor: (obj.color != "" && obj.color != undefined) ? obj.color : "red",
                                        color: "black",
                                        clickable: false
                                    });
                                    cur_function_polys.push(pg);
                                    //注记
                                    var showLableVal = data.features[i].properties[obj.label];
                                    if (obj.label != null && showLableVal != undefined) {
                                        var myIcon = new AIMap.divIcon({
                                            className: 'my-div-icon',
                                            html: "<div class='Icontitle'>" + showLableVal + "</div>"
                                        });
                                        var zj = new AIMap.Marker(pg.getBounds().getCenter(), {
                                            icon: myIcon
                                        });
                                        cur_function_polys.push(zj);
                                    }
                                }
                            }
                            break;
                    }
                    var geometryCollection = new AIMap.featureGroup(cur_function_polys);
                    map.addLayer(geometryCollection);
                    layerGroup.push({
                        type: 'vector',
                        name: obj.name,
                        layer: geometryCollection,
                        identify: obj.identify,
                        title: obj.title,
                        layerViewCenter: obj.layerViewCenter,
                        path: obj.path
                    });

                   if(obj.identify){
                     res = "";
                     loadAjax(layerGroup[layerGroup.length - 1]);
                   }
                }
            }
        });
    } else {
        removeLayer(obj.name);
        removeLocation();
    }
}

//移除图层。'all'代表所有，'name：例如yx'代表单个
function removeLayer(obj) {
    if (layerGroup.length == 0) {
        return;
    }
    if (obj == "all") {
        for (var i = 0; i < layerGroup.length; i++) {
            map.removeLayer(layerGroup[i].layer);
        }
        layerGroup = [];
    } else {
        var needdel = [];
        for (var i = 0; i < layerGroup.length; i++) {
            if (layerGroup[i].name == obj) {
                map.removeLayer(layerGroup[i].layer);
                needdel.push(layerGroup[i]);
            }
        }
        for (var i = 0; i < needdel.length; i++) {
            layerGroup.removeCurArr(needdel[i]);
        }
    }
}


function mapClick(e, lat, lng) {
    var obj = layerGroup[layerGroup.length - 1];
    if (layerGroup.length > 0 && obj.identify) {
        var curPoint = null;
        if (e == null) {
            curPoint = [lng, lat];
        } else {
            lng = e.latlng.lng;
            lat = e.latlng.lat;
            curPoint = [lng, lat];
        }
        var _returnstr = LocationAndGetAttrs(obj, curPoint);

        var displayLayerList = "";
        for (var i = 0; i < layerGroup.length; i++) {
            if (layerGroup[i].identify) {
                displayLayerList += layerGroup[i].title + ",";
            }
        }
        if (_returnstr != "") {
           //图层名称集合，解析结果，坐标
            android.skip(displayLayerList.substr(0, displayLayerList.length - 1), _returnstr, curPoint.join());
        }
    }
}

//选中当前选择图层的属性查询结果
function changeTopAttr(layertitle, curPointStr) {
    var curPoint = [curPointStr.split(",")[0], curPointStr.split(",")[1]];
    if (layertitle.length > 0) {
        for (var i = 0; i < layerGroup.length; i++) {
            if (layerGroup[i].title == layertitle) {
                res = "";
                loadAjax(layerGroup[i]);
                var attrs = LocationAndGetAttrs(layerGroup[i], curPoint);
                if (attrs != "") {
                    android.returnCurLayerDc(attrs);
                }
                break;
            }
        }
    }
}

//设置图层置顶
function setLayerTop(layertitle) {
    if (layertitle.length > 0) {
        for (var i = 0; i < layerGroup.length; i++) {
            if (layerGroup[i].title == layertitle) {
                layerGroup[i].layer.bringToFront();
                break;
            }
        }
    }
}

//设置图层透明度
function setLayerOpacity(layertitle, value) {
    if (layertitle.length > 0) {
        for (var i = 0; i < layerGroup.length; i++) {
         console.log(layerGroup[i].title);
            if (layerGroup[i].title == layertitle) {
                if(layerGroup[i].type == "raster"){
                    layerGroup[i].layer.setOpacity(value / 100);
                }
                else if(layerGroup[i].type == "vector"){
                    layerGroup[i].layer.setStyle({
//                        fillOpacity: value / 100
                        fillOpacity: value / (100 * 2)
                    });
                    break;
                }
            }
        }
    }
}


var res ;
//点查匹配方法 根据点坐标
function LocationAndGetAttrs(obj, curPoint) {
    removeLocation();
    var _returnstr = "";
    if(res != "" && res != undefined){
       for (var i = 0; i < res.features.length; i++) {
                var geoJson = AIMap.geoJson(res.features[i].geometry);
                var _lengthRes = AIMapPip.pointInLayer(curPoint, geoJson);
                if (_lengthRes.length) {
                    var zuobiaoArray = res.features[i].geometry.coordinates;
                    var lll = [];
                    for (var ii = 0; ii < zuobiaoArray.length; ii++) {
                        var _ll1 = [];
                        var _t1 = zuobiaoArray[ii];
                        for (var ii2 = 0; ii2 < _t1.length; ii2++) {
                            var _ll12 = [];
                            var _t12 = _t1[ii2];
                            if (_t12.length > 0 && _t12[0] instanceof (Array)) {
                                for (var ij2 = 0; ij2 < _t12.length; ij2++) {
                                    var _t22 = _t12[ij2];
                                    var p2 = new AIMap.LatLng(_t22[1], _t22[0], true);
                                    _ll12.push(p2);
                                }
                                _ll1.push(_ll12);
                            } else {
                                var p2 = new AIMap.LatLng(_t12[1], _t12[0], true);
                                _ll1.push(p2);
                            }
                        }
                        lll.push(_ll1);
                    }
                    if (zuobiaoArray.length == 1) {
                        lll = lll[0];
                    }

                    locationPolygon = new AIMap.Polygon(lll, {
                        fillOpacity: 0.3,
                        fillColor: '#00FFFF',
                        color: '#00FFFF',
                        clickable: false,
                        weight: 10
                    });
                    map.addLayer(locationPolygon);
                    map.setView(locationPolygon.getBounds().getCenter(), map.getBoundsZoom(locationPolygon.getBounds()));
                    var shuxing = res.features[i].properties;
                    _returnstr = "{";
                    for (var key in shuxing) {
                        _returnstr += "\"" + key + "\":\"" + shuxing[key] + "\",";
                    }
                    _returnstr = _returnstr.substr(0, _returnstr.length - 1);
                    _returnstr += "}";
                    break;
                }
            }
    }
    console.log(_returnstr);
    return _returnstr;
}


//预加载  ajax相关
function loadAjax(obj){
  //var obj = layerGroup[layerGroup.length - 1];
    if(res == "" || res == undefined){
        $.ajax({
           url: obj.path + obj.name + ".txt",
           dataType: "json",
           async: false,
           success: function (data) {
               res = data;
           }
       });
    }
}




function mapDragEnd(e) {
    var _a1 = e.target.dragging._draggable._startPos;
    var _b1 = e.target.dragging._draggable._newPos;
    var _a = Math.abs(_a1.x - _b1.x);
    var _b = Math.abs(_a1.y - _b1.y);
    if (_a < 30 && _b < 30) {
        var _sreenPoint = e.target.dragging._draggable._startPoint;
        var test = map.containerPointToLayerPoint(_sreenPoint);
        var layerPoint = map.layerPointToLatLng(test);
        lat = layerPoint.lat;
        lng = layerPoint.lng;
        mapClick(null, layerPoint.lat, layerPoint.lng);
    }
}
function removeLocation() {
    if (locationPolygon && map.hasLayer(locationPolygon)) {
        map.removeLayer(locationPolygon);
        locationPolygon = null;
    }
}
//移除数组中当前元素
Array.prototype.removeCurArr = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) {
            this.splice(i, 1);
            break;
        }
    }
}