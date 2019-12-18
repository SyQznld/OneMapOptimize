var map = null,
    mapdataPath =null,
    offsetX,offsetY,
    projStr =""; 
//var initData = {
//    arcgisTilesConfig: {
//        originXY: [-5123200, 10002100],
//        mapExtent: [542883.27463643206, 3872998.7887040703, 592883.39553378848, 3918998.859377759],
//        resolutions: [33.866734400135471, 16.933367200067735, 8.4666836000338677, 4.2333418000169338, 2.1166709000084669, 1.0583354500042335, 0.52916772500211673, 0.26458386250105836],
//        center: [3893660.6168656987, 560687.9011091357]
//    },
//    mapZoomConfig: {
//        maxZoom: 6,
//        minZoom: 0,
//        zoom: 2
//    },
//    baseLayer:"yx",
//    proj4:"+proj=tmerc +lat_0=0 +lon_0=114 +k=1.000000 +x_0=500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs"
//}

var mapConfig = {
    origin: null,
    maxExtent: null,
    crs: null,
    center: null,
    init: function (obj) {
        this.origin = new AIMap.Point(obj.originXY[0], obj.originXY[1]);
        this.maxExtent = new AIMap.Bounds(new AIMap.Point(obj.mapExtent[0], obj.mapExtent[1]), new AIMap.Point(obj.mapExtent[2], obj.mapExtent[3]));
        this.crs = new AIMap.Proj.CRS(
            'EPSG:4326', '+proj=longlat +ellps=GRS80 +no_defs', {
                origin: [this.origin.x, this.origin.y],
                resolutions: obj.resolutions,
                bounds: this.maxExtent
            });
        this.center = this.maxExtent.getCenter(false);
        this.center = this.crs.unproject(this.center);
        this.center = [this.center.lat, this.center.lng];
        if(obj.center && obj.center.length > 0){
            this.center = obj.center;
        }
    }
};
$(function () {
    mapdataPath = GetQueryString("SDPath") + "/mapdata/";
    $.ajax({
        url: mapdataPath + 'MapConfig.txt',
        dataType: "json",
        async: false,
        success: function (data) {
            loadMap(data);
            if(map){
                //初始化属性查询
                map.on("click", mapClick);
                map.on("dragend", mapDragEnd);
            }
        },
        error: function (a, b) {
            console.log("没有找到app地图配置文件!");
            return;
        }
    });
    
});

function loadMap(data) {
    var a = data.arcgisTilesConfig,
        b = data.mapZoomConfig,
        c = data.baseLayer;
        projStr = data.proj4;
        offsetX = data.offsetX;
        offsetY = data.offsetY;
    mapConfig.init(a);
    map = new AIMap.Map('map', {
        crs: mapConfig.crs,
        maxZoom: b.maxZoom,
        center: mapConfig.center,
        zoom: b.zoom,
        minZoom: b.minZoom,
        attributionControl: false,
        zoomControl: false,
        touchZoom: true,
        preferCanvas: true
    });
    android.loadBaseLayer(c);
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return "";
}
function getMapdataUrl(layername) {
    return layername + "/Layers/_alllayers/";
}