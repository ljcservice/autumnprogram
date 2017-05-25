var menuskin = "skin1"; // skin0, or skin1

function voidF(){return false;}
var JillPopupMenuItem = function(Parent, MenuID, MenuText, MenuImage, MenuFunc,MenuEnable)
{
	this.MenuID             = MenuID;
	this.Owner              = Parent;
	this.MenuText           = MenuText;
	this.MenuImage          = MenuImage;
	this.MenuFunc           = MenuFunc;
	this.Enabled            = false;
	if(MenuEnable)
		this.Enabled        = MenuEnable;
	
	this.setEnabled = function(en)
	{
		//this.Enabled = en; 
		if (this.Enabled == true)
			this.ItemTR.className = 'CM_Option';
		else
			this.ItemTR.className = 'CM_Disabled';
	}
	
	// 鼠标进入菜单
	this.Over = function()
	{
		if ( this.className != 'CM_Disabled' )
			this.className = 'CM_Over' ;
	}
	
	// 鼠标离开菜单
	this.Out = function ()
	{
		if ( this.className != 'CM_Disabled' )
			this.className = 'CM_Option' ;
	}

	this.ItemTR             = document.createElement("TR");
	this.ItemTR.id          = MenuID;
	this.ItemTR.className   = "CM_Option";
	this.ItemTR.onmouseover = this.Over;
	this.ItemTR.onmouseout  = this.Out;
		
	this.ItemTD1            = document.createElement("TD");
	this.ItemTD1.className  = "CM_Icon";

	this.ItemImg            = document.createElement("IMG");
	this.ItemImg.id         = MenuID + "Img";
	this.ItemImg.src        = MenuImage;
	
	this.ItemTD2            = document.createElement("TD");
	this.ItemTD2.className  = "CM_Label";
	this.ItemTD2.Owner      = this;
	this.ItemTD2.onclick    = function()
	{
		if (this.Owner.Enabled)
			eval(MenuFunc);
	};
	this.ItemTD2.innerText  = MenuText;

	this.ItemTD1.appendChild(this.ItemImg);
	this.ItemTR.appendChild(this.ItemTD1);
	this.ItemTR.appendChild(this.ItemTD2);
	
	this.Owner.tBodies[0].appendChild(this.ItemTR);
}

var JillPopupMenu = function(id, aWidth)
{
	this.MenuID     = id;
	this.MenuObj    = null;
	this.container  = null;
	this.Items      = new Array();
	this.iWidth     = 140;
	if (arguments.length >= 2)
		this.iWidth = aWidth;
	
	// 显示右键菜单
	this.Show = function()
	{
		var rightedge  = document.body.clientWidth  - event.clientX;
		var bottomedge = document.body.clientHeight - event.clientY;
		if (rightedge < this.MenuObj.offsetWidth)
			this.MenuObj.style.left = document.body.scrollLeft + event.clientX - this.MenuObj.offsetWidth;
		else
			this.MenuObj.style.left = document.body.scrollLeft + event.clientX;
		if (bottomedge < this.MenuObj.offsetHeight)
			this.MenuObj.style.top  = document.body.scrollTop + event.clientY - this.MenuObj.offsetHeight;
		else
			this.MenuObj.style.top  = document.body.scrollTop + event.clientY;
		this.MenuObj.style.visibility = "visible";
		this.MenuObj.style.display    = "";
		
		for (var i = 0; i < this.Items.length; i++)
		{
			if (typeof(this.Items.length) == 'undefined')
				break;
			var Item = this.Items[i];
			Item.setEnabled(arguments[i]);
		}
		return false;
	}
	
	// 关闭右键菜单
	this.Hide = function(bClear)
	{
		if (bClear)
			this.clear();
		this.MenuObj.style.visibility = "hidden";
		return false;
	}
	//清空菜单项
	this.clear= function()
	{
		var parentNode = this.container.tBodies[0];
		while (parentNode.firstChild) {
      		var oldNode = parentNode.removeChild(parentNode.firstChild);
     	}
	}
	
	this.Create = function()
	{
		document.write("<table id='" + this.MenuID + "_menu' style='TABLE-LAYOUT:\"\";border: 1px solid #8f8f73;display:none' cellpadding=0 cellspacing=2 border=0>");
		document.write("  <tr>");
		document.write("    <td>");
		document.write("      <table id='" + this.MenuID + "_MenuItems' border=0 width=" + this.iWidth + " bgcolor=white cellpadding=0 cellspacing=0>");
		document.write("      </table>");
		document.write("    </td>");
		document.write("  </tr>");
		document.write("</table>");
	
		this.MenuObj   = document.getElementById(this.MenuID + "_menu");
		this.container = document.getElementById(this.MenuID + "_MenuItems");
		this.MenuObj.className = menuskin;
	}
	
	this.addMenu = function (MenuID, MenuText, MenuImage, MenuFunc)
	{
		if (this.container == null) return;
		var MenuEnable = false;
		if (MenuFunc != '') MenuEnable = true;
		var Item = new JillPopupMenuItem(this.container, MenuID, MenuText, MenuImage, MenuFunc,MenuEnable);
		this.Items[this.Items.length] = Item;
	}

	// 在右键菜单里增加一个分隔符
	this.addSeparator = function ()
	{
		if (this.MenuObj == null) return;
		var ItemTR = document.createElement("TR");
		ItemTR.className = "CM_Separator";
	
		var ItemTD1 = document.createElement("TD");
		ItemTR.appendChild(ItemTD1);
		ItemTD1.className = "CM_Icon";
		
		var ItemTD2       = document.createElement("TD");
		ItemTR.appendChild(ItemTD2);
		ItemTD2.className = "CM_Label";
		ItemTD2.innerHTML = "<div></div>";
		this.container.tBodies[0].appendChild(ItemTR);
	}
	
	this.Create();
}