

/*
    Javascript class to handle the representation of a competition
*/
function Competition(matchID, placeholder) {

    var initiated = false;
    var ID = matchID;
    var placeholderElement = placeholder;
    var name = "";
    var club = "";
    var shots = [];
        
    this.getTotal = function() {
        var total = 0;
        for(i in shots) {
            total += shots[i];
        }
        return total;
    }
    
    function getTotal() {
        var total = 0;
        for(i in shots) {
            total += shots[i];
        }
        return total / 10;
    }
    
    this.getForecast = function() {
        var forecast = this.getTotal();
        forecast = 60 * (forecast / shots.length);
        return forecast;
    }
    
    function getForecast() {
        var forecast = getTotal();
        forecast = 60 * (forecast / shots.length);
        return forecast;
    }
    
    this.update = function() {
        if(!initiated) {
            buildCompetitionElement();
            getCompetitionInfo();
            initiated = true;
        } 
        updateTargetImage();
        updateShotData();
    }
    
    /*
        Private methods. 
    */
    function getCompetitionInfo() {
        var infoUrl = "rest/public/getCompetition/?competitionID=" + ID;
        $.getJSON(infoUrl, {}, function(competition) {
            name = competition.shooter.shooterName;
            club = competition.shooter.clubName;
            $("#" + ID + "-shooterName").html(name);
            $("#" + ID + "-shooterClub").html(club);
        });
    }
    
    function updateShotData() {
        var shotsUrl = "rest/public/getShots/?competitionID=" + ID;
        $.getJSON(shotsUrl, {}, function(data) {
            shots = data.slice();
        }).done(function() {
            refreshShotsDisplay();
        });
    }
    
    function updateTargetImage() {
        var targetUrl = "rest/public/getZoomedScoringTarget/?competitionID=" + ID;
        $("#" + ID + "-targetImg").hide();
        $("#" + ID + "-targetImg").attr("data", targetUrl);
        $("#" + ID + "-targetImg").show();
    }
    
    function refreshShotsDisplay() {
        var total = getTotal();
        $("#" + ID + "-total").html(total.toFixed(1));
        var forecast = getForecast();
        $("#" + ID + "-prognosis").html(forecast.toFixed(1));
        updateSeriesElements();
        updateLastSeriesElement();
    }
    
    function updateLastSeriesElement() {
        var j = 1;
        var seriesFound = false;
        while(!seriesFound) {
            if(shots.length > (j*10)) {
                j++;
            } else {
                seriesFound = true;
            }
        }
        var startIdx = (j-1) * 10;
        for(var i = 0; i < 10; i++) {
            var val = 0;
            $("#" + ID + "-LS" + (i+1)).empty();
            $("#" + ID + "-LS" + (i+1) + "C").html(startIdx+i+1 + ".");
            if((startIdx+i) < shots.length) {
                val = shots[startIdx+i];
                $("#" + ID + "-LS" + (i+1)).html((val / 10).toFixed(1));
            } else {
                $("#" + ID + "-LS" + (i+1)).html("&nbsp;");
            }
        }
    }
    
    function updateSeriesElements() {
        var seriesCount = 1;
        var shotCount = 0;
        var runningTotal = 0;
        for(i in shots) {
            runningTotal += shots[i];
            shotCount++;
            if((shotCount % 10) == 0) {
                $("#" + ID + "-S" + seriesCount).empty();
                $("#" + ID + "-S" + seriesCount).html((runningTotal / 10).toFixed(1));
                runningTotal = 0;
                seriesCount++;
            }
        }
        // Update the last series
        $("#" + ID + "-S" + seriesCount).empty();
        $("#" + ID + "-S" + seriesCount).html((runningTotal / 10));
    }
    
    
    /*
        Elements that the is taken:
        - #ID-shooterID         -- For the shooterID element
        - #ID-lastSeries        -- For the last series element
        - #ID-totals            -- For the totals element
        - #ID-series            -- For all series element
        - #ID-target            -- For the target element
        - #ID-shooterName       -- For the shooter name
        - #ID-shooterClub       -- For the shooter club
        - #ID-LS{1-10}          -- For the last shot, 1 through 10
        - #ID-total             -- For the total value
        - #ID-prognosis         -- For the prognosis value
        - #ID-S{1-6}            -- For the last series, 1 through 6
        - #ID-targetImg         -- For the image of the target
    
    */
    
    function buildCompetitionElement() {
        $("#" + placeholderElement).html(buildSkeleton());
        $("#" + ID + "-shooterID").html(buildShooterIDElement());
        $("#" + ID + "-lastSeries").html(buildLastSeriesElement());
        $("#" + ID + "-totals").html(buildTotalsElement());
        $("#" + ID + "-series").html(buildSeriesElement());
        $("#" + ID + "-target").html(buildTargetElement());    
    }
    
    function buildSkeleton() {
        var skeletonHtml = "<div class='row'>";
            skeletonHtml += "<div class='col-md-6'>";
                skeletonHtml += "<div class='row' id='" + ID + "-shooterID'></div>";
                skeletonHtml += "<div class='row'>";
                    skeletonHtml += "<div class='col-md-6' id='" + ID + "-lastSeries'></div>";
                    skeletonHtml += "<div class='col-md-6' id='" + ID + "-totals'></div>";
                skeletonHtml += "</div>";
                skeletonHtml += "<div class='row'>";
                    skeletonHtml += "<div class='col-md-12' id='" + ID + "-series'></div>";
                skeletonHtml += "</div>";
            skeletonHtml += "</div>";
            skeletonHtml += "<div class='col-md-6' id='" + ID + "-target'></div>";
        skeletonHtml += "</div>";
        return skeletonHtml;
    }
    
    function buildShooterIDElement() {
        var shooterIDHtml = "<div class='col-md-6' id='" + ID + "-shooterName'> Skytte: </div>";
        shooterIDHtml += "<div class='col-md-6' id='" + ID + "-shooterClub'> Klub: </div>";
        return shooterIDHtml;
    }
    
    function buildLastSeriesElement() {
        var lastSeriesHtml = "Sidste serie: <table>";
        lastSeriesHtml += "<tr><td><i id='" + ID + "-LS1C' >1.</i></td><td id='" + ID + "-LS1'></td><td> &nbsp; </td><td><i id='" + ID + "-LS6C'>6.</i></td><td id='" + ID + "-LS6'></td></tr>";
        lastSeriesHtml += "<tr><td><i id='" + ID + "-LS2C'>2.</i></td><td id='" + ID + "-LS2'></td><td> &nbsp; </td><td><i id='" + ID + "-LS7C'>7.</i></td><td id='" + ID + "-LS7'></td></tr>";
        lastSeriesHtml += "<tr><td><i id='" + ID + "-LS3C'>3.</i></td><td id='" + ID + "-LS3'></td><td> &nbsp; </td><td><i id='" + ID + "-LS8C'>8.</i></td><td id='" + ID + "-LS8'></td></tr>";
        lastSeriesHtml += "<tr><td><i id='" + ID + "-LS4C'>4.</i></td><td id='" + ID + "-LS4'></td><td> &nbsp; </td><td><i id='" + ID + "-LS9C'>9.</i></td><td id='" + ID + "-LS9'></td></tr>";
        lastSeriesHtml += "<tr><td><i id='" + ID + "-LS5C'>5.</i></td><td id='" + ID + "-LS5'></td><td> &nbsp; </td><td><i id='" + ID + "-LS10C'>10.</i></td><td id='" + ID + "-LS10'></td></tr>";
        lastSeriesHtml += "</table>";
        return lastSeriesHtml;
    }
    
    function buildTotalsElement() {
        var totalsHtml = "<table>";
        totalsHtml += "<tr><td><i>Total:</i></td><td id='" + ID + "-total'></td></tr>";
        totalsHtml += "<tr><td><i>Prognose:</i></td><td id='" + ID + "-prognosis'></td></tr>";
        return totalsHtml;
    }
    
    function buildSeriesElement() {
        var seriesHtml = "<table class='table table-bordered table-condensed'>";
        seriesHtml += "<thead><tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr></thead>";
        seriesHtml += "<tbody><tr><td id='" + ID + "-S1'></td><td id='" + ID + "-S2'></td>";
        seriesHtml += "<td id='" + ID + "-S3'></td><td id='" + ID + "-S4'></td>";
        seriesHtml += "<td id='" + ID + "-S5'></td><td id='" + ID + "-S6'></td></tbody>";
        seriesHtml += "</table>"
        return seriesHtml;
    }
    
    function buildTargetElement() {
        var targetUrl = "rest/public/getZoomedScoringTarget/?competitionID=" + ID;
        var targetHtml = "<object id='" + ID + "-targetImg' type='image/svg+xml' data='" + targetUrl + "' style='width:175pt'></object>";
        return targetHtml;
    }
    
}
