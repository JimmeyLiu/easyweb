!function ($) {
    'use strict';
    var LwpChart = function (el, options) {
        this.options = options;
        this.$el = $(el);
        this.$el_ = this.$el.clone();
        this._chart = undefined,
        this.init();
    };

    var initParamConfig = function(fixedParams, paramConfig){
        var name = paramConfig.name;
        var input = paramConfig.input;
        var config = paramConfig.config;
        var fixed = false;
        $.each(fixedParams,function(k,v){
            if(k == name){
                fixed = true;
            }
        })

        if(fixed){
            return '';
        }

        if('text' == input){
            return '<input size="20" type="text" name="'+name+'" placeholder="'+name+'">';
        }
        if('date' == input){
            return '<input size="20" type="text" class="day-datetime" name="'+name+'" placeholder="'+name+'">';
        }
        if('select' == input){
            var tmp = [];
            tmp.push('<input size="20" type="text" name="');
            tmp.push(name);
            tmp.push('"placeholder=" ');
            tmp.push(config);
            tmp.push('" autocomplete="off" data-provide="typeahead" data-source="[');
            if(config){
                var opts = config.split(',');
                var start = true;
                $.each(opts,function(i,v){
                    if(start){
                        start = false;
                    }else{
                        tmp.push(',');
                    }
                    tmp.push('&quot;'+v+'&quot;');
                })
            }

            tmp.push(']">');
            return tmp.join('');
        }
    }

    var initAreaRange = function(el,data){
        var chart = new Highcharts.Chart({
            chart: {
                renderTo : el,
                type: 'arearange',
                zoomType: 'x'
            },

            title: {
                text: ' '
            },

            xAxis: {
                type: 'datetime'
            },

            yAxis: {
                title: {
                    text: null
                }
            },
            series:data.series
        });
    }

    var initLineChart = function(el,data){
        var chart = new Highcharts.Chart({
            chart : {
                renderTo : el,
                zoomType : 'x',
                spacingRight : 20
            },
            title: {
                text: ''
            },
            xAxis : {
                type : 'datetime'
            },
            yAxis : {
                title : {
                    text : ' '
                }
            },
            credits:{
                enabled:false
            },
            legend:{
                maxHeight:60
            },

            plotOptions : {
                spline : {
                    lineWidth : 1,
                    states : {
                        hover : {
                            lineWidth : 1
                        }
                    },
                    marker : {
                        enabled : false,
                        states : {
                            hover : {
                                enabled : true,
                                symbol : 'circle',
                                radius : 3,
                                lineWidth : 1
                            }
                        }
                    }
                },
                series:{
                    turboThreshold:5000
                }
            },
            tooltip : {
                shared : true,
                crosshairs : true,
                formatter : function() {
                        var s = '<b>'
                            + Highcharts.dateFormat('%Y-%m-%d %H:%M:%S',
                                    this.x) + '</b>';
                        jQuery.each(this.points, function(i, point) {
                            s += '<br/><span style="color:'
                                + point.series.color + '">'
                                + point.series.name.split(" ")[0]
                                + '</span>: ' + point.y;
                        });
                        return s;
                }
            },
            series : data.series
        });
    };

    var initColumnChart = function(el,data,stacking){
        var chart = new Highcharts.Chart({
            chart: {
                renderTo : el,
                type: 'column',
                zoomType : 'x'
            },
            credits:{
                enabled:false
            },
            title: {
                text: ''
            },
            xAxis: {
                categories: data.categories
            },
            yAxis: {
                min: 0,
                title : {
                    text : ' '
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
                shared: true
            },
            plotOptions: {
                column: {
                    borderWidth: 0,
                    pointPadding: 0.2,
                    stacking:stacking
                }
            },
            series: data.series
        });
    };

    var initTable = function(el,data){
        var p = $('#'+el);
        var t = $('#'+el+'_table');
        if(t.length != 0){
            $('#'+el+'_table').bootstrapTable('load',data.data);
        }else{
            p.append('<table id="'+el+'_table"></table>')
            $('#'+el+'_table').bootstrapTable({
                minimumCountColumns: 2,
                striped: true,
                pagination: true,
                pageSize: 10,
                columns: data.columns,
                search:true,
                data:data.data
            });
        }
    };

    var initMap = function(id,result){
        var seriesAry = result.series;
        if(seriesAry){
            seriesAry.forEach(function(e) {
                e.itemStyle= {normal:{label:{show:true}},emphasis:{label:{show:true}}};
            });
        }

        var option = {
            title : {
                text: result.title,
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter:function (params,ticket,callback) {
                    var res = params.name +'<br/>';
                    if (params.seriesName && params.value >= 0){
                        if (params.seriesName) {
                            res +=  params.seriesName + ' : ';
                        }
                        res +=params.value;
                    } else {
                        res+="无";
                    }

                    if (params.data && params.data.moreDes && params.data.moreDes.length > 0) {
                        res+="<br/>"+params.data.moreDes;
                    }
                    setTimeout(function (){
                      // 仅为了模拟异步回调
                        callback(ticket, res);
                    }, 0)
                    return res;
                }
            },
            dataRange: {
                min: result.min,
                max: result.max,
                x: 'right',
                y: 'bottom',
                calculable : true,
                color:['#009100','#00DB00','#53FF53','#CEFFCE','#EA0000']
            },
            series :seriesAry
        };
        var curTheme = {};
        if (myChart && myChart.dispose) {
            myChart.dispose();
        }
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(option, true)
    };

    var drawChart = function(chartType,domId,data){
        if('line' == chartType){
            initLineChart(domId,data);
        }else if('pie' == chartType){
            initPieChart(domId,data);
        }else if('column' == chartType){
            initColumnChart(domId,data,'normal');
        }else if('column_percent' == chartType){
            initColumnChart(domId,data,'percent');
        }else if('map' == chartType){
            initMap(domId,data);
        }else if('table' == chartType){
            initTable(domId,data);
        }else if('area_range' == chartType){
             initAreaRange(domId,data);
         }
    };

    LwpChart.DEFAULTS = {
        method: 'get',
        url: undefined,
        dataType: 'json',//json or jsonp
        chartType:undefined,
        fixedParams: {},
        paramConfigs:[],
        height:400,
        data:undefined
    };

    LwpChart.prototype.init = function () {
        this.initQueryForm();
        this.initChart();
        this.bindReload();
    };

    LwpChart.prototype.initQueryForm = function(){
        if($.isEmptyObject(this.options.paramConfigs)){
            return;
        }
        var html = [];
        var count = 0;
        var $this = this;
        html.push('<form class="inline">');
        $.each(this.options.paramConfigs, function (i, pc) {
            var input = initParamConfig($this.options.fixedParams,pc);
            if(input != ''){
                html.push(input);
                count++;
            }
        });
        if(count == 0){
            return;
        }
        html.push('<button class="btn btn-primary btn-xs reload">');
        html.push('<i class="icon-search align-top bigger-125"></i>GO');
        html.push('</button></form>');
        this.$el.html(html.join(''));
        var $this = this;
        var id = this.$el.attr('id')+"_chart";
        this.$el.find('.reload').click(function(e){
            e.preventDefault();
            var f = $this.$el.find('form').serializeArray();
            var ps = {};//
            $.each(f,function(i,v){
                ps[v.name] = v.value;
            })
            ps = $.extend(ps,$this.options.fixedParams);
            jQuery.ajax({
                url : $this.options.url,
                dataType : $this.options.dataType,
                data:ps,
                success : function(data) {
                    drawChart($this.options.chartType,id,data);
                }
            });
        });
    };

    LwpChart.prototype.initChart = function(){
        var chartType = this.options.chartType;
        var id = this.$el.attr('id')+"_chart";
        this.$el.append('<div id="'+id+'" style="height:'+this.options.height+'px"></div>');
        if(this.options.data != undefined){
            drawChart(chartType,id,this.options.data);
        }else{
            jQuery.ajax({
                url : this.options.url,
                dataType : this.options.dataType,
                data:this.options.fixedParams,
                success : function(data) {
                    drawChart(chartType,id,data);
                }
            });
        }
    }

    LwpChart.prototype.bindReload = function(){
        var _this = this;
        $('#'+this.$el.attr('id')+"-reload").click(function(e){
            e.preventDefault();
            if(_this.options.data != undefined){
                return;
            }
            var chartType = _this.options.chartType;
            var id = _this.$el.attr('id')+"_chart";
            jQuery.ajax({
                url : _this.options.url,
                dataType : _this.options.dataType,
                data:_this.options.fixedParams,
                success : function(data) {
                    drawChart(chartType,id,data);
                }
            });
        })
    }

    var allowedMethods = [
        'load'
    ];
    $.fn.lwpChart = function (option, _relatedTarget) {
        var value;

        this.each(function () {
            var $this = $(this),
                data = $this.data('lwp.chart'),
                options = $.extend({}, LwpChart.DEFAULTS, $this.data(),
                    typeof option === 'object' && option);

            if (typeof option === 'string') {
                if ($.inArray(option, allowedMethods) < 0) {
                    throw "Unknown method: " + option;
                }

                if (!data) {
                    return;
                }

                value = data[option](_relatedTarget);

                if (option === 'destroy') {
                    $this.removeData('lwp.table');
                }
            }

            if (!data) {
                $this.data('lwp.chart', (data = new LwpChart(this, options)));
            }
        });

        return typeof value === 'undefined' ? this : value;
    };

    $.fn.lwpChart.Constructor = LwpChart;
    $.fn.lwpChart.defaults = LwpChart.DEFAULTS;
    $.fn.lwpChart.methods = allowedMethods;
}(jQuery)