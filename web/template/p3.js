  function publishThis() {
      Parse.initialize("hGNzYptoQo0eLE4NNjYrom3xoRvr6zeNPkhUtSbI", "PEFd0WblDlUrUKyPC0VEdkR4xyQY21i8SyghPr8J");
      var TestObject = Parse.Object.extend("Article");
      var testObject = new TestObject();
      testObject.save({
          title: document.getElementById("inputTitle").value
      });
      testObject.save({
          excerpt: document.getElementById("inputExcerpt").value
      });
      console.log("fora do test");
      testObject.save(null, {

          success: function (object) {
              $(".success").show();
              console.log("dentro do test");
              // document.getElementById("qrCode").src = ("http://chart.googleapis.com/chart?cht=qr&chs=400x400&choe=UTF-8&chld=H|0&chl=" + object.id);
              document.getElementById("qrDownload").href = ("http://chart.googleapis.com/chart?cht=qr&chs=800x800&choe=UTF-8&chld=H|0&chl=" + object.id);
              document.getElementById("tTitle").innerHTML= '' + object.get("title");
              document.getElementById("pResumo").innerHTML='Resumo: ' + object.get("excerpt");
              $("#qrPreview").css('background-image', 'url(http://chart.googleapis.com/chart?cht=qr&chs=150x150&choe=UTF-8&chld=H|0&chl=' +  object.id +')' );
          },
          error: function (model, error) {
              $(".error").show();
          }
      });

      return false;
  }
