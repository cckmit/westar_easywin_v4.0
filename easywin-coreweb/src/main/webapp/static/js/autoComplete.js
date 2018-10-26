/**
 * 自动补全js
 */

function autoCompleteCallBack(flag){
	//alert(1);
}

var $id = function (id) {
	//避免与jQuery的$函数冲突  
    return typeof id == "string" ? document.getElementById(id) : id;  
}; 

var Bind = function(object, fun) {
    return function() {
        return fun.apply(object, arguments);
    }
}
function AutoComplete(obj,hiddenId,autoObj,arr){
    this.obj=$id(obj);        //输入框
    this.autoObj=$id(autoObj);//DIV的根节点
    this.value_arr=arr;        //不要包含重复值
    this.index=-1;          //当前选中的DIV的索引
    this.search_value="";   //保存当前搜索的字符
    this.hiddenId = hiddenId; //返回值隐藏对象ID
}
AutoComplete.prototype={
    //初始化DIV的位置
    init: function(){
        //this.autoObj.style.left = this.obj.left + "px";
       // this.autoObj.style.top  = this.obj.top + this.obj.offsetHeight + "px";
       // this.autoObj.style.width= "40%";//减去边框的长度2px
	//alert(this.autoObj.style.offsetLeft);
    },
    //删除自动完成需要的所有DIV
    deleteDIV: function(){
        while(this.autoObj.hasChildNodes()){
            this.autoObj.removeChild(this.autoObj.firstChild);
        }
        this.autoObj.className="auto_hidden";
    },
    //设置值
    setValue: function(_this){
        return function(){
            _this.obj.value=this.seq;
            $("#"+_this.hiddenId).val(this.objId);
            _this.autoObj.className="auto_hidden";
            autoCompleteCallBack(_this.hiddenId);
        }
    },
    //模拟鼠标移动至DIV时，DIV高亮
    autoOnmouseover: function(_this,_div_index){
        return function(){
            _this.index=_div_index;
            var length = _this.autoObj.children.length;
            for(var j=0;j<length;j++){
                if(j!=_this.index ){
                    _this.autoObj.childNodes[j].className='auto_onmouseout';
                }else{
                    _this.autoObj.childNodes[j].className='auto_onmouseover';
                }
            }
        }
    },
    //更改classname
    changeClassname: function(length){
        for(var i=0;i<length;i++){
            if(i!=this.index ){
                this.autoObj.childNodes[i].className='auto_onmouseout';
            }else{
                this.autoObj.childNodes[i].className='auto_onmouseover';
                this.obj.value=this.autoObj.childNodes[i].seq;
            }
        }
    }
    ,
    //响应键盘
    pressKey: function(event){
        var length = this.autoObj.children.length;
        //光标键"↓"
        if(event.keyCode==40){
            ++this.index;
            if(this.index>length){
                this.index=0;
            }else if(this.index==length){
                this.obj.value=this.search_value;
            }
            this.changeClassname(length);
        }
        //光标键"↑"
        else if(event.keyCode==38){
            this.index--;
            if(this.index<-1){
                this.index=length - 1;
            }else if(this.index==-1){
                this.obj.value=this.search_value;
            }
            this.changeClassname(length);
        }
        //回车键
        else if(event.keyCode==13){
            this.autoObj.className="auto_hidden";
            this.index=-1;
        }else{
            this.index=-1;
        }
    },
    //程序入口
    start: function(event){
        if(event.keyCode!=13&&event.keyCode!=38&&event.keyCode!=40){
            this.init();
            this.deleteDIV();
            this.search_value=this.obj.value;
            var valueArr=this.value_arr;
            var strJson = eval('(' + valueArr + ')');
            strJson.sort();
            if(this.obj.value.replace(/(^\s*)|(\s*$)/g,'')==""){ return; }//值为空，退出
            try{ var reg = new RegExp("(" + this.obj.value + ")","i");}
            catch (e){ return; }
            var div_index=0;//记录创建的DIV的索引
            for(var i=0;i<strJson.length;i++){
                if(reg.test(strJson[i].name)){
                    var div = document.createElement("div");
                    div.className="auto_onmouseout";
                    div.seq=strJson[i].name;
                    div.objId=strJson[i].id;
                    div.onclick=this.setValue(this);
                    div.onmouseover=this.autoOnmouseover(this,div_index);
                    div.innerHTML=strJson[i].name.replace(reg,"<strong style='color:red'>$1</strong>");//搜索到的字符粗体显示
                    this.autoObj.appendChild(div);
                    this.autoObj.className="auto_show";
                    div_index++;
                }
            }
        }
        this.pressKey(event);
        window.onresize=Bind(this,function(){this.init();});
    }
}