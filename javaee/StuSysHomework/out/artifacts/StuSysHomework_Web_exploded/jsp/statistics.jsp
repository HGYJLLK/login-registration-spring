<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2025/11/6
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生信息统计 - 学生信息管理系统</title>
    <!-- Font Awesome -->
    <link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- Chart.js（保留图表库，如需删除可移除） -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.8/dist/chart.umd.min.js"></script>
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
        }

        /* 按钮样式 */
        .btn {
            padding: 8px 16px;
            border-radius: 6px;
            font-weight: 500;
            transition: all 0.2s;
            border: none;
            cursor: pointer;
            outline: none;
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
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .stat-info p:first-child {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 4px;
        }
        .stat-info p:last-child {
            font-size: 24px;
            font-weight: bold;
            color: #1f2937;
        }
        .stat-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        /* 内容网格 */
        .content-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        @media (max-width: 768px) {
            .content-grid {
                grid-template-columns: 1fr;
            }
        }

        /* 图表容器 */
        .chart-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .chart-wrapper {
            height: 300px;
            margin-top: 16px;
        }

        /* 表格样式 */
        .table-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px 16px;
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
        tbody tr:hover {
            background-color: #f9fafb;
        }

        /* 返回按钮容器 */
        .back-container {
            margin-top: 32px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 页面标题 -->
    <div class="text-center mb-8">
        <h1>按性别统计学生数量</h1>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>总学生数</p>
                <p id="totalStudents">0</p>
            </div>
            <div class="stat-icon" style="background-color: #dbeafe; color: #3b82f6;">
                <i class="fa fa-users"></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>男生数量</p>
                <p id="maleStudents">0</p>
            </div>
            <div class="stat-icon" style="background-color: #dbeafe; color: #3b82f6;">
                <i class="fa fa-mars"></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>女生数量</p>
                <p id="femaleStudents">0</p>
            </div>
            <div class="stat-icon" style="background-color: #fce7f3; color: #ec4899;">
                <i class="fa fa-venus"></i>
            </div>
        </div>
    </div>

    <!-- 统计图表和表格 -->
    <div class="content-grid">
        <!-- 统计图表 -->
        <div class="chart-container">
            <h2>性别分布饼图</h2>
            <div class="chart-wrapper">
                <canvas id="genderChart"></canvas>
            </div>
        </div>

        <!-- 统计表格 -->
        <div class="table-container">
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
                    <td id="maleCount">0</td>
                    <td id="malePercentage">0%</td>
                </tr>
                <tr>
                    <td>女</td>
                    <td id="femaleCount">0</td>
                    <td id="femalePercentage">0%</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 返回按钮 -->
    <div class="back-container">
        <button id="backBtn" class="btn btn-primary">
            <i class="fa fa-arrow-left mr-1"></i> 返回学生列表
        </button>
    </div>
</div>

<script>
    // DOM元素
    const totalStudentsElement = document.getElementById('totalStudents');
    const maleStudentsElement = document.getElementById('maleStudents');
    const femaleStudentsElement = document.getElementById('femaleStudents');
    const maleCountElement = document.getElementById('maleCount');
    const femaleCountElement = document.getElementById('femaleCount');
    const malePercentageElement = document.getElementById('malePercentage');
    const femalePercentageElement = document.getElementById('femalePercentage');
    const backBtn = document.getElementById('backBtn');

    // 初始化页面
    document.addEventListener('DOMContentLoaded', function() {
        setupEventListeners();
        // 后续对接MySQL时，这里会调用加载统计数据的方法
        // loadStatistics();
    });

    // 设置事件监听器
    function setupEventListeners() {
        // 返回学生列表
        backBtn.addEventListener('click', function() {
            window.location.href = 'index.jsp';
        });
    }

    // 后续对接MySQL时需要实现的方法（示例）
    /*
    function loadStatistics() {
        // 1. 发送AJAX请求到后端获取统计数据
        // 2. 更新统计卡片和表格
        // 3. 渲染图表
    }

    function renderChart(maleCount, femaleCount) {
        const ctx = document.getElementById('genderChart').getContext('2d');
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['男', '女'],
                datasets: [{
                    data: [maleCount, femaleCount],
                    backgroundColor: ['#3B82F6', '#EC4899'],
                    borderColor: '#ffffff',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false
            }
        });
    }
    */
</script>
</body>
</html>