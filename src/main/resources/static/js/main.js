    function openTab(index) {
        // Hide all tab panes
        $('.tab-pane').removeClass('active show');
        // Show the selected tab pane
        $('#tab' + index).addClass('active show');
        // Update the active state of the tab links
        $('.nav-tabs a').removeClass('active');
        $('.nav-tabs a[href="#tab' + index + '"]').addClass('active');
    }
    function sendEmail(index) {

      $.ajax({
      url: "/sendEmail",
      beforeSend:function() {
         $(".overlay").fadeIn(300);
      },
      success: function(result){
      $.parseHTML(result)
        //$(".container .my-2").html(result);
        $("#confirmText").html("Emails Sent Success");
        $("#confirmModal").modal();
        $(".overlay").fadeOut(300);
      },
      failure:function(e){
      console.log(e);
      },
      complete: function (data) {
       $(".overlay").fadeOut(300);
          }
      });

      }

