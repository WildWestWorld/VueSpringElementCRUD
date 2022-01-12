<template>
  <div>
    <div id="container" style="width: 100%;height:100%"></div>
  </div>
</template>

<script>
let map;
export default {
  name: "Map",
  data() {
    return {

    }
  },
  mounted() {

    // 百度地图API功能
    map = new BMap.Map('container'); // 创建Map实例
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 12); // 初始化地图,设置中心点坐标和地图级别
    map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放

    // var geolocation = new BMap.Geolocation();
    // // 开启SDK辅助定位
    // geolocation.enableSDKLocation();
    // geolocation.getCurrentPosition(function(r){
    //   if(this.getStatus() == BMAP_STATUS_SUCCESS){
    //     var mk = new BMap.Marker(r.point);
    //     map.addOverlay(mk);
    //     map.panTo(r.point);
    //     alert('您的位置：'+r.point.lng+','+r.point.lat);
    //   }
    //   else {
    //     alert('failed'+this.getStatus());
    //   }
    // });

    function myFun(result){
      var cityName = result.name;
      map.setCenter(cityName);
      alert("当前定位城市:"+cityName);
    }
    var myCity = new BMap.LocalCity();
    myCity.get(myFun);
  }
}

</script>

<style scoped>

</style>
