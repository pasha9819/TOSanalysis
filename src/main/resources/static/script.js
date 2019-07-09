document.addEventListener("DOMContentLoaded", function(event){
    window.onresize = function() {
        loadData();
    };

    loadStops();
    $( function() {
        var sliderDiv = $( "#slider-range" );
        sliderDiv.slider({
            range: true,
            min: 11,
            max: 48,
            values: [ 11,48 ],
            slide: function( event, ui ) {
                sliderDiv.slider( "values", [ ui.values[0], ui.values[1] ] );
                $( "#time_value" ).text( parseTime(ui.values[0]) + " - " + parseTime(ui.values[1]));
                loadData();
            }
        });

        $( "#time_value" ).text(
            parseTime(sliderDiv.slider("values", 0))
            + " - "
            + parseTime(sliderDiv.slider("values", 1)));
    } );
});



function parseTime(timeValue) {
    if (Number(timeValue) === 48){
        return "23:59";
    }
    var hour = Math.floor( Number(timeValue) / 2 );
    var answer = "";
    if (hour < 10) {
        answer = "0";
    }
    answer += hour + ":";
    if (Number(timeValue) % 2 === 1) {
        answer += "30";
    }else {
        answer += "00";
    }
    return answer;
}

function loadStops() {
    $.ajax({
        url: "getObservedStops",
        type: "GET",
        success: function (data) {
            var routeSelector = $("select[id='stop']");
            routeSelector.empty();
            routeSelector.append(data);
            loadRoutes();
        }
    });
}

function loadRoutes() {
    var stopSelector = $("select[id='stop']");
    var stopId = stopSelector.val();
    $.ajax({
        url: "getRouteByStop",
        type: "GET",
        data: ({
            stop_id: stopId
        }),
        success: function (data) {
            var routeSelector = $("select[id='route']");
            routeSelector.empty();
            routeSelector.append(data);
            loadData();
        }
    });

}

function loadData() {
    var stopId = $("select[id='stop']").val();
    var routeId = $("select[id='route']").val();
    var timeValues = $( "#slider-range" ).slider( "values" );
    var start = parseTime(timeValues[0]);
    var end = parseTime(timeValues[1]);
    $.ajax({
        url: "getAccuracyData",
        type: "GET",
        data: ({
            stop_id: stopId,
            route_id: routeId,
            start: start,
            end: end
        }),
        success: function (data) {
            drawGraphic(data);
        }
    });
}

function  drawGraphic(points) {
    var max = 0;
    var i;
    for(i = 0; i < points.length; i++){
        var y = points[i].y;
        var x = points[i].x;
        if(y > max){
            max = y;
        }
        if(x > max){
            max = x;
        }
    }
    var diag = [];
    var step = max / 1000;
    for(i = 0; i < max; i+=step){
        diag.push({x: i, y:i});
    }

    var ctx = document.getElementById('myChart').getContext('2d');
    var scatterChart = new Chart(ctx, {
        type: 'scatter',
        data: {
            datasets: [{
                radius: 5,
                data: points
            },
                {
                    type: "line",
                    radius: 1,
                    backgroundColor: 'rgba(0,0,0,1)',
                    data: diag //[{x: 0, y:0}, {x: max, y:max}]
                }]
        },
        options: {
            animation:{
                duration: 0
            },
            events: [],
            legend: {
                display: false
            },
            title: {
                display: true,
                fontSize: 20,
                text: "Точность прогнозов"
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        fontSize: 18,
                        labelString: "Реальное время (c)"
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        fontSize: 18,
                        labelString: "Прогноз (c)"
                    }
                }]
            }
        }
    });
}