<html>
<head>
	<title>personList</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
	<table border=1>
		<tr>
			<th>序号</th>
			<th>id</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>性别</th>
		</tr>
		<#list personList as person>
		<#if person_index % 2 == 0>
		<tr bgcolor="orange">
		<#else>
		<tr bgcolor="blue">
		</#if>
			<td>${person_index + 1}</td>
			<td>${person.id}</td>
			<td>${person.name}</td>
			<td>${person.age}</td>
			<td>${person.sex}</td>			
		</tr>
		</#list>
	</table>
	<hr/>
	当前日期:${date?date}<br/>
	当前日期:${date?time}<br/>
	当前日期:${date?datetime}<br/>
	当前日期:${date?string("yyyy/MM/dd HH:mm:ss")}<br/>
	<hr/>
	null值的处理:${val!"null"}<br/>
	null值的判断:
	<#if val??>
	val中有值
	<#else>
	val是个null
	</#if>
	<hr/>
	引用模板测试:<br/>
	<#include "hello.ftl">
</body>
</html>