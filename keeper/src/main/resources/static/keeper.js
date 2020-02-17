$(document).ready(function() {
    principal.user();
    goalcontroller.list();

    $("#logout input").val(Cookies.get("XSRF-TOKEN"));
    $("#logout a").click(function() {
        $("#logout form").submit();
    });

    $("#new button")
        .click(goalcontroller.add);
    $("#new input")
        .keypress(function (e) { if (e.which == 13) { goalcontroller.add() }});
});

$.ajaxSetup({
    beforeSend : function(xhr, settings) {
        if (settings.type == 'POST' || settings.type == 'PUT'
            || settings.type == 'DELETE') {
            if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
                // Only send the token to relative URLs i.e. locally.
                xhr.setRequestHeader("X-XSRF-TOKEN", Cookies.get('XSRF-TOKEN'));
            }
        }
    }
});

let principal = {
    user : function() {
        $.get("/user")
            .done(function(user) {
                $("#welcome").html(user + "'s Goals");
            })
    }
};

let goalcontroller = {
    list : function() {
        $.get("/goals")
            .done(function(goals) {
                for (let goal of goals) {
                    goalcontroller._upsertGoal(goal);
                }
            });
    },
    add : function() {
        $.ajax({
                type : "POST",
                url : "/goal",
                data : $("#new input").val(),
                contentType: "application/json"
            })
            .done(function(goal) {
                goalcontroller._upsertGoal(goal);
                $("#new input").val("");
            });
    },
    toggle : function(id) {
        $.ajax({
            type : "PUT",
            url : "/goal/" + id + "/toggle",
            })
            .done(function(goal) {
                goalcontroller._upsertGoal(goal);
            })
    },
    _upsertGoal : function(goal) {
        let li = $("#goals li").filter(function() { return $(this).data("id") == goal.id; });
        if (li.length == 0) {
            li = $("<li class='list-group-item'>");
            li.data("id", goal.id);
            li.click(function() {
               goalcontroller.toggle(li.data("id"));
            });
            li.hover(function() {
                $(this).toggleClass("active");
            });
            $("#goals").append(li);
        }
        li.text(goal.text);
        if (goal.completed) {
            li.addClass("completed");
        } else {
            li.removeClass("completed");
        }
    }
};