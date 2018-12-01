package rest

import org.w3c.xhr.XMLHttpRequest

class Client {
    var xhttp= XMLHttpRequest();

    fun fetch(url:String, header: String ="application/hal+json",
              call: (String) -> Unit) {
        xhttp.open("GET", url, true);
        xhttp.setRequestHeader("Accept", header)
        xhttp.onload = {
            call(xhttp.responseText)
        }
        xhttp.send();
    }
	
	fun getMethod(url:String, content: String, header: String ="application/hal+json", call: (String) -> Unit) {
        xhttp.open("GET", url, true);
        xhttp.setRequestHeader("Accept", header)
        xhttp.onload = {
            call(xhttp.responseText)
        }
        xhttp.send(content);
    }
	
    fun post(url:String, content:String, call: (String) -> Unit = {}) {
        xhttp.open("POST", url, true);
        xhttp.setRequestHeader("Content-Type", "application/hal+json")
        xhttp.onload = {
            call(xhttp.responseText)
        }
        xhttp.send(content)
    }

    fun delete(url:String, call: (String) -> Unit = {}){
        xhttp.open("DELETE", url, true);
        xhttp.setRequestHeader("Content-Type", "application/hal+json")
        xhttp.onload = {
            call(xhttp.responseText)
        }
        xhttp.send();
    }


//    var xhttp= XMLHttpRequest();
//
//    fun fetch(url:String, header: String ="application/hal+json",
//              call: (String) -> Unit) {
//        xhttp.open("GET", url, true);
//        xhttp.setRequestHeader("Accept", header)
//        xhttp.onload = {
//            call(xhttp.responseText)
//        }
//        xhttp.send();
//    }
//
//    fun updateETag(url:String, header: String ="application/hal+json", emp: JsonEmployee, call: () -> Unit ={}) {
//        xhttp.open("GET", url, true);
//        xhttp.setRequestHeader("Accept", header)
//        xhttp.onload = {
//            console.log(xhttp.getResponseHeader("ETag"))
//            //val version = xhttp.getResponseHeader("ETag")
//            call()
//        }
//
//        xhttp.send();
//    }
//
//    fun post(url:String, entity:String, call: (String) -> Unit = {}) {
//        xhttp.open("POST", url, true);
//        xhttp.setRequestHeader("Content-Type", "application/hal+json")
//        xhttp.onload = {
//            call(xhttp.responseText)
//        }
//        xhttp.send(entity);
//    }
//
//    fun put(url:String, entity:String, etag: Long , call: (String)-> Unit) {
//        //path: employee.entity._links.self.href,
//        xhttp.open("PUT", url, true);
//        xhttp.setRequestHeader("Content-Type", "application/json")
//        xhttp.setRequestHeader("If-Match", "$etag")
//        xhttp.onload = {
//            if(xhttp.status.toInt() == 412) {
//                window.alert("DENIED: Unable to update $url. Your copy is stale.")
//            }else {
//                call(xhttp.responseText)
//            }
//        }
//        xhttp.send(entity);
//    }
//
//    fun delete(url:String, call: (String) -> Unit = {}){
//        xhttp.open("DELETE", url, true);
//        xhttp.setRequestHeader("Content-Type", "application/hal+json")
//        xhttp.onload = {
//            call(xhttp.responseText)
//        }
//        xhttp.send();
//    }
}

