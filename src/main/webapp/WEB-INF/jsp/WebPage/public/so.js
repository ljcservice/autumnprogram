// JavaScript Document
function setHomepage(obj,url)
{
	obj.style.behavior='url(#default#homepage)';
	obj.setHomePage(url);
}
function CheckAll()
{
	for (var i=0;i<document.recruitfrm.elements.length;i++)
	{
		var e = document.recruitfrm.elements[i];
		if (e.name == 'mailist[]')
			e.checked = document.recruitfrm.chkall.checked;
	}
	document.recruitfrm.chkall1.checked = document.recruitfrm.chkall.checked;
}
function CheckAll1()
{
	for (var i=0;i<document.recruitfrm.elements.length;i++)
	{
		var e = document.recruitfrm.elements[i];
		if (e.name == 'mailist[]')
			e.checked = document.recruitfrm.chkall1.checked;
	}
	document.recruitfrm.chkall.checked = document.recruitfrm.chkall1.checked;
}

function bookmark()
{
	//window.external.AddFavorite('http://so.jobmet.com/', '!')
}

function doZoom(size)
{
	document.getElementById('zoom').style.fontSize=size+'px';
}

function copyUrl()
{
	var content='';
	content=window.top.location+"&ctrlvadvice=1";
	content+= '\r\n , Щλ, о, ,:),~~';
	copyToClipboard(content);
	//window.clipboardData.setData("Text",content);
	//alert(" CTRL+V QQ  MSN  *^-^* ");
}

function copyToClipboard(meintext)
{
	if (window.clipboardData) 
	{
		// the IE-way
		window.clipboardData.setData("Text", meintext);
	}
	// Probabely not the best way to detect netscape/mozilla.
	// I am unsure from what version this is supported
	else
	if (window.netscape)
	{
		// This is importent but it's not noted anywhere
		netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		// create interface to the clipboard
		var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
		if (!clip) return;
		// create a transferable
		var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
		if (!trans) return;
	// specify the data we wish to handle. Plaintext in this case. trans.addDataFlavor('text/unicode'); // To get the data from the transferable we need two new objects var str = new Object(); var len = new Object();
	var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
	var copytext=meintext; str.data=copytext; trans.setTransferData("text/unicode",str,copytext.length*2); var clipid=Components.interfaces.nsIClipboard; if (!clip) return false; clip.setData(trans,null,clipid.kGlobalClipboard); }
	alert(" CTRL+V QQ  MSN  *^-^*");
	return false; 
}
function hidnode(type)
{
	var count = document.getElementById("C_"+type+"_cont");
	var sub = document.getElementById("C_"+type+"_sub");
	var plus = document.getElementById("C_"+type+"_plus");
	sub.style.display = "none";
	plus.style.display = "";
	count.style.display = "none"
}
function shownode(type)
{
	var count = document.getElementById("C_"+type+"_cont");
	var sub = document.getElementById("C_"+type+"_sub");
	var plus = document.getElementById("C_"+type+"_plus");
	sub.style.display = "";
	plus.style.display = "none";
	count.style.display = ""
}

function clicknode(type)
{
	var sub = document.getElementById("C_"+type+"_sub");
	if(sub.style.display ==""){
		hidnode(type);
	}else{
		shownode(type);
	}
}
function pSearch(kind)
{
	var str = $('query').value;
	//location.href = "http://www.dayanmei.com/search.php?action=search&keyword=" + str;	
}