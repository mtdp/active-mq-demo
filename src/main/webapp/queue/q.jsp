<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>char demo page</title>
<script type="text/javascript" src="../js/lib/jquery1.8.1.js"></script>
<script type="text/javascript" src="../js/lib/echarts-all.js"></script>
<style type="text/css">
	.sDiv{
		text-align: center;
		height: 500px;
		line-height: 500px;
		margin-top:150px;
	}
</style>
</head>
<body>
	<div id="main" style="height:400px;weight:200px">
	</div>
	<script type="text/javascript">
		var mLen = 30;
		var myChart = echarts.init(document.getElementById('main'));
		function getInitData(){
			var value = [];
			$.ajax({
				type:'POST',
				url:"/MQ/hq/initData.do",
				data:{"limit":mLen},
				dataType:"json",
				async:false,
				success:function(e){
					if("success" == e.code){
						//console.log(e);
						value = e.obj;
						//console.log(value);
					}
				}
			});
			return value;
		}
		function getLastData(){
			var value = 100;
			$.ajax({
				type:'POST',
				url:"/MQ/hq/lastData.do",
				data:{"limit":mLen},
				dataType:"json",
				async:false,
				success:function(e){
					if("success" == e.code){
						//console.log(e);
						value = e.obj;
						//console.log(value);
					}
				}
			});
			return value;
		}
		
		function formatTime(d){
			return d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
		}
		
		var option = {
		    title : {
		        text: 'hornetq队列数据',
		        subtext: '169环境'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['队列消息堆积数量', '队列消息总数量']
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    dataZoom : {
		        show : false,
		        start : 0,
		        end : 100
		    },
		    xAxis : [
		        //显示时间
		        {
		            type : 'category',
		            boundaryGap : true,
		            data : (function (){
		                var now = new Date();
		                var res = [];
		                var len = mLen;
		                while (len--) {
		                    //res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
		                    res.unshift(formatTime(now));
		                    now = new Date(now - 2000);
		                }
		                return res;
		            })()
		        },
		        //显示序号
		        {
		            type : 'category',
		            boundaryGap : true,
		            data : (function (){
		                var res = [];
		                var len = mLen;
		                while (len--) {
		                    res.push(len + 1);
		                }
		                return res;
		            })()
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            scale: true,
		            name : '消息数量',
		            boundaryGap: [0.2, 0.2]
		        },
		        {
		            type : 'value',
		            scale: true,
		            name : '消息数量',
		            boundaryGap: [0.2, 0.2]
		        }
		    ],
		    series : [
		        {
		            name:'队列消息总数量',
		            type:'bar',
		            xAxisIndex: 1,
		            yAxisIndex: 1,
		            data:(function (){
		                var res = getInitData();
		                /* var len = mLen;
		                //填充准备数据
		                while (len--) {
		                    res.push(Math.round(Math.random() * 1000));
		                } */
		                return res;
		            })()
		        },
		        {
		            name:'队列消息堆积数量',
		            type:'line',
		            data:(function (){
		                var res = getInitData();
		                /* var len = mLen;
		                //填充准备数据
		                while (len--) {
		                    res.push((Math.random()*10 + 5).toFixed(1) - 0);
		                } */
		                return res;
		            })()
		        }
		    ]
		};
		//var lastData = 11;
		var axisData;
		//clearInterval(timeTicket);
		var timeTicket = setInterval(function (){
		    /* lastData += Math.random() * ((Math.round(Math.random() * 10) % 2) == 0 ? 1 : -1);
		    lastData = lastData.toFixed(1) - 0; */
		    //lastData = getLastData();
		    //axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');
		    axisData = (formatTime(new Date()));
		    // 动态数据接口 addData
		    myChart.addData([
		        [
		            0,        // 系列索引
		            Math.round(Math.random() * 1000), // 新增数据
		            true,     // 新增数据是否从队列头部插入
		            false     // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
		        ],
		        [
		            1,        // 系列索引
		            getLastData(), // 新增数据
		            false,    // 新增数据是否从队列头部插入
		            false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
		            axisData  // 坐标轴标签
		        ]
		    ]);
		}, 2000);
		
		myChart.setOption(option);
	</script>
</body>
</html>