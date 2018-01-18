<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
${hello}
<label>id</label>${stu.id}<br/>
<label>name</label>${stu.name}<br/>
<label>age</label>${stu.age}<br/>
<table border="1">
<tr><td>序号</td><td>学号</td><td>姓名</td><td>年龄</td></tr>
<#list stulist as student>
<#if student_index % 2 == 0>
<tr bg color = "blue">
<#else>
<tr bg color = "red">
</#if>
<td>${student_index}</td><td>${student.id}</td><td>${student.name}</td><td>${student.age}</td></tr>
</#list>
</table>
<br>
当前时间：${date?date}<br/>
当前时间：${date?time}<br/>
当前日期和时间：${date?datetime}<br/>
自定义日期格式：${date?string("yyyyMMddHHmmss")}<br/>
null值的处理：${myval!"defaultValue"}
null值的处理：
<#if myval2??>
	myval2 not null
<#else>
	myval2 is null
</#if>
<br/>
包含其他模版 相对路径
<#include "./other.ftl" />
</body>
</html>