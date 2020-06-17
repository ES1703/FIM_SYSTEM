<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<script>
	window.onload = function() {
		var dateControl = document.querySelector('input[id="start_date"]');
		dateControl.value = '2017-06-01T08:30';
	}
	$(function() {
		var chart = {

			credits : {
				enabled : false
			//不顯示LOGO
			},
			title : {
				text : '溫度'
			},
			xAxis : [ {
				type : 'datetime',
				labels : {
					format : '{value:%H:%M}'
				},
				tickPixelInterval : 120
			} ],
			yAxis : {
				title : {
					text : '攝氏 (°C)'
				},
				max : 100,
				min : 0
			},
			plotOptions : {
				line : {
					dataLabels : {
						enabled : true
					},
					enableMouseTracking : true
				}
			},
			series : requestData()
		};

		function requestData() {
			var series = new Array();
			$.ajax({
				url : "showChartDht11",
				type : "GET",
				async : false,
				success : function(data) {
					var seriesData = [];
					for (var j = 0; j < data[0].length; j++) {
						var point = data[0][j];
						var time = point[0];
						var value = point[1];
						seriesData.push({
							x : time,
							y : value
						});
					}
					series.push({
						"name" : "溫濕度",
						"data" : seriesData
					});
				}

			});
			return series;

		}
		$("#searchBtn").off().on("click", function() {
			var charSert = Highcharts.chart('container2', chart);
		});

		$("#resetBtn").off().on("click", function() {
			var charSert = Highcharts.chart('container2', chart);
		});
	})
</script>

<div id="page-wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">溫濕度報表</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="row">
						<table class="table table-bordered"">
							<tbody>
								<tr class="text-center">
									<!-- 感應裝置名稱 -->
									<td>查詢期間</td>
									<td><input class="form-control" type="datetime-local"
										value="" id="start_date"></td>
									<td>至</td>
									<td><input class="form-control" type="datetime-local"
										value="" id="end_date"></td>
								</tr>
								<tr>
									<td class="text-center" colspan="4" class="heading">
										                                    
										<button type="submit" class="btn btn-primary btn-sm"
											id="searchBtn">
											<i class="fa fa-search fa-large"></i>查询
										</button>                                      
										<button type="reset" class="btn btn-primary btn-sm"
											id="resetBtn" style="margin-right: 30px;">
											<i class="fa fa-undo fa-large"></i>重置
										</button>                                 
									</td>
								</tr>

							</tbody>
						</table>
					</div>
					<!-- /.row (nested) -->
					<div id="container2"></div>
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-12 -->
	</div>
</div>

