(function(mainApp) {
  mainApp(window.jQuery, window, document);
}(function($, window, document) {

  $(function() {

    // Auto-connect
    initConnect();

    $('#chat-send').click(function(event) {
      var obj = { cmd: "pch", data: { msg: $('#chat-input').val() } };
      var data = JSON.stringify(obj);
      wsocket.send(data);

      console.log(data);
      console.log(wsocket);
    });

    // Navigation
    $('.nav a.register').click(function(event) {
      $.colorbox({
        href: "/register",
        onComplete: function(){
          $('#register-form input#username').focus();
          regRegisterButton();
        }
      });
      event.preventDefault();
    });

    $('.nav a.login').click(function(event) {
      $.colorbox({
        href: "/login",
        onComplete: function(){
          $('#login-form input#username').focus();
          regLoginButton();
        }
      });
      event.preventDefault();
    });

    $('.nav a.donate').click(function(event) {
      window.open(this.href);
      event.preventDefault();
    });

    $('.nav a.contact').click(function(event) {
      $.colorbox({
        href: "/contact",
        onComplete: function(){
          $('#contact-form input#email').focus();
          regContactButton();
        }
      });
      event.preventDefault();
    });




  });

  // Register clicks
  function regRegisterButton() {
    $('#register-form .btn-submit').click(function(event) {
      submitRegister();
      event.preventDefault();
    });
  }

  function regLoginButton() {
    $('#login-form .btn-submit').click(function(event) {
      submitLogin();
      event.preventDefault();
    });
  }

  function regContactButton() {
    $('#contact-form .btn-submit').click(function(event) {
      submitContact();
      event.preventDefault();
    });
  }

  var wsocket = {};

  function initConnect() {
    wsocket = new WebSocket("ws://localhost:9000/ws");
    console.log(wsocket);

    wsocket.onopen = function (event) {
      console.log("ws opened");
      //wsocket.send("stretch");
    };

    wsocket.onmessage = function (event) {
      console.log("ws received: " + event.data);
      var msg = JSON.parse(event.data);
      console.log(msg);
      processMessage(msg);
    };

    wsocket.onclose = function (event) {
      console.log("ws closed");
    };
  }

  function processMessage(msg) {
    switch (msg.cmd) {
      case "pch":
        processChatMsg(msg.data);
        break;
      case "tch":
        processChatMsg(msg.data);
        break;
      case "aup":
        console.log("aup");
        break;
      default:
        console.log("default");
    }
  }

  function processChatMsg(msg) {
    var out = '<div class="chat_msg ' + msg.cls + '"><span class="name">' +
      msg.snd + '</span><span class="msg">' + msg.msg + '</span></div>';

    console.log(out);
    $('#chat-msgs').append(out);
  }

  function submitRegister() {
    var r = jsRoutes.controllers.Register.checkRegister();
    var jdata = JSON.stringify($('#register-form').serializeObject());
    return $.ajax({
      url: r.url,
      type: r.type,
      contentType: "application/json",
      data: jdata,
      success: function(tpl) {
        document.forms["register-form"].submit();
        console.log(tpl);
      },
      error: function(err) {
        $('#cboxLoadedContent').html(err.responseText);
        regRegisterButton();
        $.colorbox.resize();
      }
    });
  }

  function submitLogin() {
    var r = jsRoutes.controllers.Login.checkLogin();
    var jdata = JSON.stringify($('#login-form').serializeObject());
    return $.ajax({
      url: r.url,
      type: r.type,
      contentType: "application/json",
      data: jdata,
      success: function(tpl) {
        document.forms["login-form"].submit();
        console.log("sent");
        console.log(tpl);
      },
      error: function(err) {
        $('#cboxLoadedContent').html(err.responseText);
        console.log("sent2");
        regLoginButton();
        $.colorbox.resize();
      }
    });
  }

  function submitContact() {
    var r = jsRoutes.controllers.Contact.submitContact();
    var jdata = JSON.stringify($('#contact-form').serializeObject());
    return $.ajax({
      url: r.url,
      type: r.type,
      contentType: "application/json",
      data: jdata,
      success: function(tpl) {
        //document.forms["contact-form"].submit();
        $('#cboxLoadedContent').html(tpl);
        $.colorbox.resize();
      },
      error: function(err) {
        $('#cboxLoadedContent').html(err.responseText);
        console.log("sent222");
        regContactButton();
        $.colorbox.resize();
      }
    });
  }

  // Helper methods
  $.fn.serializeObject = function()
  {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
      if (o[this.name] !== undefined) {
        if (!o[this.name].push) {
          o[this.name] = [o[this.name]];
        }
        o[this.name].push(this.value || '');
      } else {
        o[this.name] = this.value || '';
      }
    });
    return o;
  };

}));
