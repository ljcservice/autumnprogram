<html lang="zh-CN">
<head>
<meta charset="utf-8">
<title>接口测试</title>
<link href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css"rel="stylesheet">
<link href="http://cdn.bootcss.com/font-awesome/4.1.0/css/font-awesome.min.css"rel="stylesheet">
<link href="http://static.bootcss.com/www/assets/css/site.min.css?v3"rel="stylesheet">
<link href="http://static.bootcss.com/www/assets/ico/favicon.png"rel="shortcut icon">
<script src="http://fuck.thinksaas.cn/get/http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
</head>
<body>
<div class="container">
<div class="row row-offcanvas row-offcanvas-right">
<div class="col-xs-12 col-sm-12">
<div class="row">
<div class="col-xs-1 col-lg-4">
<h1>接口测试</h1>
<div class="thumbnail">
<form class="form-signin"action=""method="post">
<b>请填URL</b>:
<input value="<?php echo isset($_POST['url'])?$_POST['url']:'';?>"class="form-control"placeholder="填写完整地址，以http://开头"type="text"name="url"required><br>
<?php if (isset($_POST['param']) && !empty($_POST['param'])) :?>
<?php foreach ($_POST['param'] as $k => $item) :?>
<?php if (!empty($item['method']) && !empty($item['name'])) :?>
<div class="thumbnail">
<b>参数name</b>:
<input value="<?php echo $item['name'];?>"placeholder="请填写"type="text"name="param[<?php echo $k;?>][name]"><br>
<b>参数value</b>:
<input value="<?php echo $item['value'];?>"placeholder="请填写"type="text"name="param[<?php echo $k;?>][value]"><br>
<b>请求方式</b>:
<label><input <?php if($item['method']=='get'):?>checked<?php endif;?> value="get"type="radio"name="param[<?php echo $k;?>][method]">get</label>
<label><input <?php if($item['method']=='post'):?>checked<?php endif;?> value="post"type="radio"name="param[<?php echo $k;?>][method]">post</label><br />
<a href="#"onclick="del_param(this)">删除</a>
</div>
<?php endif;?>
<?php endforeach;?>
<?php endif;?>

<input type="button"name="add_param"id="add_param"value="添加参数"class="btn btn-lg btn-primary btn-block"><br />
<input type="submit"name="submit"value="测试"class="btn btn-lg btn-primary btn-block"><br />
</form>
</div>
</div>
<div class="col-xs-1 col-lg-8">
<?php
if (isset($_POST['submit'])) {
echo"<pre>";
echo"请求时间:";
var_dump($end - $start);

echo"<br />请求url:";
isset($url) && var_dump($url);

echo"<br />请求参数:";
isset($param) && var_dump($param);

echo"<hr />结果：";
if (isset($content['result'])) {
echo"<br />code:";
var_dump($content['result']['code']);
echo"message:";
var_dump($content['result']['message']);
echo"data:";
var_dump($content['result']['data']);
} else {
echo $content;
}

echo"</pre>";
}
?>
</div>
</div>
</div>
</div>
<hr />
</div>
<div class="blog-masthead">
<div class="container">
<nav class="blog-nav">
<p class="blog-nav-item">&copy; Company 2014</p>
</nav>
</div>
</div>
</body>
</html>

<script>
$("#add_param").click(function(){
var input_len = $("form input").size();
input_len++;
$(this).before('
<div class="thumbnail">
<b>参数name</b>:
<input value=""placeholder="请填写"type="text"name="param['+ input_len +'][name]"><br>
<b>参数value</b>:
<input value=""placeholder="请填写"type="text"name="param['+ input_len +'][value]"><br>
<b>请求方式</b>:
<label><input checked value="get"type="radio"name="param['+ input_len +'][method]">get</label>
<label><input value="post"type="radio"name="param['+ input_len +'][method]">post</label><br />
<a href="#"onclick="del_param(this)">删除</a>
</div>
');
});
function del_param(obj) {
$(obj).parent().remove();
}
</script>
