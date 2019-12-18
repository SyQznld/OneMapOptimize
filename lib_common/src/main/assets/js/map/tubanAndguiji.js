var selectedPolygonTb = null;
//绘制标注面  传入坐标点以及键值对信息（java中拼接好）
function drawBzPolygon(_geom, info) {
	removeSelectedPolygon();
	var PolygonPoint = _geom.split(",");
	var _ll = [];
	for (var ii = 0; ii < PolygonPoint.length; ii++) {
		var n = PolygonPoint[ii].split(" ");
		var n1 = n[0];
		var n2 = n[1];
		_ll.push([n2, n1]);
	}
	selectedPolygonTb = new AIMap.Polygon(_ll, {
			name: name,
			weight: 3,
			fillOpacity: 0.2,
			fillColor: "#40E0D0",
			color: "yellow",
			clickable: true
		});
	map.addLayer(selectedPolygonTb);
	var totalhtml = "<span style='font-size: 1.3rem;'>";
	for (var key in info) {
		var _value = info[key];
		totalhtml += key + "：" + _value + "<br/>";
	}
	totalhtml += "</span>";
	selectedPolygonTb.bindPopup(totalhtml).openPopup();
	map.setView(selectedPolygonTb.getBounds().getCenter(), map.getZoom());
}


//绘制标注线
function drawBzLine(_geom, info) {
	removeSelectedPolygon();
	var PolygonPoint = _geom.split(",");
	var _ll = [];
	for (var ii = 0; ii < PolygonPoint.length; ii++) {
		var n = PolygonPoint[ii].split(" ");
		var n1 = n[0];
		var n2 = n[1];
		_ll.push([n2, n1]);
	}
	selectedPolygonTb = new AIMap.Polyline(_ll, {
			color: '#00E5EE',
			weight: 6,
			lineCap: 'round',
			lineJoin: 'round'
		}).addTo(map);
	map.addLayer(selectedPolygonTb);
	var totalhtml = "<span style='font-size: 1.3rem;'>";
	for (var key in info) {
		var _value = info[key];
		totalhtml += key + "：" + _value + "<br/>";
	}
	totalhtml += "</span>";
	selectedPolygonTb.bindPopup(totalhtml).openPopup();
	map.setView(selectedPolygonTb.getBounds().getCenter(), map.getZoom());
}



var track = null;
var start_icon = null;
var end_icon = null;
var trackPoint = [];
//绘制轨迹,根据已知集合字符串，定时绘制，包含动画
function drawListTrack(arrayList, name, remark, time) {
	if (track && map.hasLayer(track)) {
		map.removeLayer(track);
	}
	if (start_icon && map.hasLayer(start_icon)) {
		map.removeLayer(start_icon);
	}
	if (end_icon && map.hasLayer(end_icon)) {
		map.removeLayer(end_icon);
	}
	var coordinateArray = [];
	var array = arrayList.split(",");
	for (var i = 0; i < array.length; i++) {
		var coordinate = Trim(array[i]);
		var list = coordinate.split(" ");
		var x = list[0];
		var y = list[1];
		var toCRS = "+proj=tmerc +lat_0=0 +lon_0=114 +k=1.000000 +x_0=500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs";
		var sss = proj4(toCRS, [x, y]);
		coordinateArray.push([sss[1] - 8, sss[0] - 117]);
	}
	track = new AIMap.Polyline(coordinateArray, {
			color: '#00E5EE',
			weight: 6,
			lineCap: 'round',
			lineJoin: 'round'
		}).addTo(map);
	map.fitBounds(track.getBounds());
	start_icon = AIMap.marker(coordinateArray[0], {
			icon: AIMap.icon({
				iconUrl: 'images/icon_point_s.png',
				iconSize: [50, 50]
			})
		}).addTo(map);
	end_icon = AIMap.marker(coordinateArray[coordinateArray.length - 1], {
			icon: AIMap.icon({
				iconUrl: 'images/icon_point_e.png',
				iconSize: [50, 50]
			})
		}).addTo(map);
	track.bindPopup(" <span style='font-size: 1.8rem;'> 轨迹名称：" + name + "<br />备注：" + remark + "<br />时间：" + time + "<br /> </span> ").openPopup();
}



function Trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}




var guiji = [];
var points = [];
//边走边绘制
function drawTrack(coordinate) {
	var list = coordinate.split(" ");
	var x = list[0];
	var y = list[1];
	var toCRS = "+proj=tmerc +lat_0=0 +lon_0=114 +k=1.000000 +x_0=500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs";
	var sss = proj4(toCRS, [x, y]);
	if (points.length >= 1) {
		var curLine = new AIMap.Polyline([points[points.length - 1], [sss[1] - 8, sss[0] - 117]], {
				color: '#00E5EE',
				weight: 6,
				lineCap: 'round',
				lineJoin: 'round'
			});
		map.addLayer(curLine);
		guiji.push(curLine);
	}
	points.push([sss[1] - 8, sss[0] - 117]);
}



function removeSelectedPolygon() {
	if (selectedPolygonTb && map.hasLayer(selectedPolygonTb)) {
		map.removeLayer(selectedPolygonTb);
	}
}
