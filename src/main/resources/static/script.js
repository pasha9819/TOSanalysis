document.addEventListener("DOMContentLoaded", function(event){
    window.onresize = function() {
        loadData();
    };
    var slider = document.getElementById("time");
    var output = document.getElementById("time_value");
    output.innerHTML = getTimeInterval(slider.value);

    slider.oninput = function() {
        output.innerHTML = getTimeInterval(this.value);
        loadData();
    };
    loadStops();
});

function getStartTime(val) {
    var v = Number(val);
    var answer;
    if (v === 1) {
        answer = "05";
    }else if (v === 2) {
        answer = "09";
    }else if (v === 3) {
        answer = "15";
    }else{
        answer = "19";
    }
    answer += ":00";
    return answer;
}

function getEndTime(val) {
    var v = Number(val);
    var answer;
    if (v === 1) {
        answer = "08";
    }else if (v === 2) {
        answer = "14";
    }else if (v === 3) {
        answer = "18";
    }else{
        answer = "23";
    }
    answer += ":59";
    return answer;
}

function getTimeInterval(val){
    return getStartTime(val) + " - " + getEndTime(val);
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
    var time = $("input[id='time']").val();
    var start = getStartTime(time) + ":00";
    var end = getEndTime(time) + ":59";
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
                        labelString: "Реальное время"
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        fontSize: 18,
                        labelString: "Прогноз"
                    }
                }]
            }
        }
    });
}