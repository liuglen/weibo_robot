system = require('system')  
address = system.args[1];
var page = require('webpage').create();  
var url = address;  

page.settings.resourceTimeout = 1000*10; // 10 seconds
page.onResourceTimeout = function(e) {
    console.log(page.content);      
    phantom.exit(1);
};

page.open(url, function (status) {  
    //Page is loaded!  
    if (status !== 'success') {  
        console.log('Unable to post!');  
    } else {
        var val = page.evaluate(function(){
            return document.querySelector('div.weibo-text').innerHTML;
        });
        //console.log(page.content);
        console.log(val);
    }
    phantom.exit(); 
  });