<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2025/11/6
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>学生信息统计 - 学生信息管理系统</title>
    <style>
        /* 基础样式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', system-ui, sans-serif;
        }
        body {
            background-color: #f9fafb;
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .text-center {
            text-align: center;
        }
        .mb-4 {
            margin-bottom: 16px;
        }
        .mb-6 {
            margin-bottom: 24px;
        }
        .mb-8 {
            margin-bottom: 32px;
        }
        h1 {
            font-size: 28px;
            font-weight: bold;
            color: #1f2937;
        }
        h2 {
            font-size: 20px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 16px;
        }

        /* 按钮样式 */
        .btn {
            padding: 10px 20px;
            border-radius: 6px;
            font-weight: 500;
            transition: all 0.2s;
            border: none;
            cursor: pointer;
            outline: none;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary {
            background-color: #4F46E5;
            color: white;
        }
        .btn-primary:hover {
            background-color: #4338ca;
        }

        /* 统计卡片 */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 32px;
        }
        .stat-card {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            padding: 24px;
            text-align: center;
        }
        .stat-card .stat-label {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 8px;
        }
        .stat-card .stat-value {
            font-size: 32px;
            font-weight: bold;
            color: #1f2937;
        }
        .stat-card.blue {
            border-top: 4px solid #3b82f6;
        }
        .stat-card.pink {
            border-top: 4px solid #ec4899;
        }

        /* 表格样式 */
        .table-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin-bottom: 32px;
        }
        .table-header {
            padding: 20px;
            border-bottom: 1px solid #e5e7eb;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 16px 20px;
            text-align: left;
            font-size: 14px;
        }
        th {
            background-color: #f3f4f6;
            font-weight: 600;
            color: #4b5563;
        }
        tbody tr {
            border-bottom: 1px solid #e5e7eb;
        }
        tbody tr:last-child {
            border-bottom: none;
        }
        tbody tr:hover {
            background-color: #f9fafb;
        }
        td {
            color: #1f2937;
        }

        /* 返回按钮容器 */
        .back-container {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 页面标题 -->
    <div class="text-center mb-8">
        <h1>学生信息统计</h1>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-label">总学生数</div>
            <div class="stat-value">${stats.total}</div>
        </div>

        <div class="stat-card blue">
            <div class="stat-label">男生数量</div>
            <div class="stat-value">${stats.maleCount}</div>
        </div>

        <div class="stat-card pink">
            <div class="stat-label">女生数量</div>
            <div class="stat-value">${stats.femaleCount}</div>
        </div>
    </div>

    <!-- 统计表格 -->
    <div class="table-container">
        <div class="table-header">
            <h2>性别分布详情</h2>
        </div>
        <table>
            <thead>
            <tr>
                <th>性别</th>
                <th>学生数量（人）</th>
                <th>占比</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>男</td>
                <td>${stats.maleCount}</td>
                <td>${stats.malePercentage}</td>
            </tr>
            <tr>
                <td>女</td>
                <td>${stats.femaleCount}</td>
                <td>${stats.femalePercentage}</td>
            </tr>
            <tr style="background-color: #f9fafb; font-weight: 600;">
                <td>总计</td>
                <td>${stats.total}</td>
                <td>100.0%</td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- 返回按钮 -->
    <div class="back-container">
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">返回学生列表</a>
    </div>
</div>
</body>
</html>
