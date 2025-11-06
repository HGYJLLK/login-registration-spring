<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生信息管理系统</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            color: #333;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #ddd;
        }

        .header h1 {
            color: #2c3e50;
            font-size: 28px;
        }

        .search-box {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .search-input {
            flex: 1;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;
        }

        .btn-primary {
            background-color: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
        }

        .btn-success {
            background-color: #27ae60;
            color: white;
        }

        .btn-success:hover {
            background-color: #219a52;
        }

        .btn-info {
            background-color: #17a2b8;
            color: white;
        }

        .btn-info:hover {
            background-color: #138496;
        }

        .btn-danger {
            background-color: #e74c3c;
            color: white;
        }

        .btn-danger:hover {
            background-color: #c0392b;
        }

        .btn-warning {
            background-color: #f39c12;
            color: white;
        }

        .btn-warning:hover {
            background-color: #d35400;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #495057;
        }

        tr:hover {
            background-color: #f8f9fa;
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal-content {
            background: white;
            border-radius: 8px;
            width: 500px;
            max-width: 90%;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        }

        .modal-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .modal-title {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
        }

        .modal-body {
            padding: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
            color: #495057;
        }

        .form-control {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .form-control:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
        }

        .border-red-500 {
            border-color: #e74c3c !important;
        }

        .text-red-500 {
            color: #e74c3c;
        }

        .text-xs {
            font-size: 12px;
        }

        .mt-1 {
            margin-top: 4px;
        }

        .modal-footer {
            padding: 20px;
            border-top: 1px solid #eee;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        .pagination {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .pagination-info {
            color: #6b7280;
            font-size: 14px;
        }

        .pagination-controls {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .pagination-btn {
            padding: 6px 12px;
            border: 1px solid #ddd;
            background: white;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }

        .pagination-btn.active {
            background-color: #3498db;
            color: white;
            border-color: #3498db;
        }

        .pagination-btn:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
        }

        .badge-success {
            background-color: #d4edda;
            color: #155724;
        }

        .badge-warning {
            background-color: #fff3cd;
            color: #856404;
        }

        .badge-danger {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>学生信息管理系统</h1>
        <div>
            <button id="addBtn" class="btn btn-success">添加学生</button>
            <a href="/statistics" class="btn btn-warning">统计分析</a>
        </div>
    </div>

    <!-- 搜索表单 -->
    <form action="/student/search" method="get" class="search-box">
        <input type="text" name="keyword" class="search-input" placeholder="请输入学号或姓名进行搜索..." value="${searchKeyword}">
        <button type="submit" class="btn btn-primary">搜索</button>
        <a href="/" class="btn">重置</a>
    </form>

    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th>学号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>出生日期</th>
                <th>手机号</th>
                <th>班级</th>
                <th>院系</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="studentTableBody">
            <c:choose>
                <c:when test="${empty students}">
                    <tr>
                        <td colspan="10" style="text-align: center; color: #6b7280; padding: 40px;">
                            暂无学生数据
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.studentNo}</td>
                            <td>${student.name}</td>
                            <td>${student.gender}</td>
                            <td>${student.age}</td>
                            <td>${student.birthDate}</td>
                            <td>${student.phone}</td>
                            <td>${student.className}</td>
                            <td>${student.department}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${student.status == '在读'}">
                                        <span class="badge badge-success">在读</span>
                                    </c:when>
                                    <c:when test="${student.status == '休学'}">
                                        <span class="badge badge-warning">休学</span>
                                    </c:when>
                                    <c:when test="${student.status == '毕业'}">
                                        <span class="badge badge-danger">毕业</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <div style="display: flex; gap: 8px;">
                                    <button class="edit-btn btn btn-info" data-id="${student.id}"
                                            data-studentno="${student.studentNo}"
                                            data-name="${student.name}"
                                            data-gender="${student.gender}"
                                            data-age="${student.age}"
                                            data-birthdate="${student.birthDate}"
                                            data-phone="${student.phone}"
                                            data-classname="${student.className}"
                                            data-department="${student.department}"
                                            data-status="${student.status}">
                                        编辑
                                    </button>
                                    <a href="/student/delete?id=${student.id}" class="btn btn-danger"
                                       onclick="return confirm('确定要删除这条学生信息吗？')">
                                        删除
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <div class="pagination">
        <div class="pagination-info">
            共 <span id="totalItems">${students.size()}</span> 条记录
        </div>
    </div>
</div>

<!-- 学生信息模态框 -->
<div id="studentModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 id="modalTitle">添加学生信息</h3>
        </div>
        <form id="studentForm" action="/student/save" method="post" onsubmit="return validateForm()">
            <div class="modal-body">
                <input type="hidden" id="id" name="id" value="">

                <div class="form-group">
                    <label class="form-label">学号 *</label>
                    <input type="text" id="studentNo" name="studentNo" class="form-control" required value="">
                </div>

                <div class="form-group">
                    <label class="form-label">姓名 *</label>
                    <input type="text" id="name" name="name" class="form-control" required value="">
                </div>

                <div class="form-group">
                    <label class="form-label">性别 *</label>
                    <select id="gender" name="gender" class="form-control" required>
                        <option value="">请选择</option>
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">年龄 *</label>
                    <input type="number" id="age" name="age" class="form-control" required value="" min="1" max="100">
                </div>

                <div class="form-group">
                    <label class="form-label">出生日期 *</label>
                    <input type="date" id="birthDate" name="birthDate" class="form-control" required value="">
                </div>

                <div class="form-group">
                    <label class="form-label">手机号 *</label>
                    <input type="tel" id="phone" name="phone" class="form-control" required value="" pattern="[0-9]{11}">
                </div>

                <div class="form-group">
                    <label class="form-label">班级 *</label>
                    <input type="text" id="className" name="className" class="form-control" required value="">
                </div>

                <div class="form-group">
                    <label class="form-label">院系 *</label>
                    <input type="text" id="department" name="department" class="form-control" required value="">
                </div>

                <div class="form-group">
                    <label class="form-label">状态 *</label>
                    <select id="status" name="status" class="form-control" required>
                        <option value="">请选择</option>
                        <option value="在读">在读</option>
                        <option value="休学">休学</option>
                        <option value="毕业">毕业</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="cancelBtn" class="btn">取消</button>
                <button type="submit" class="btn btn-primary">保存</button>
            </div>
        </form>
    </div>
</div>

<script>
    // 表单验证函数
    function validateForm() {
        const form = document.getElementById('studentForm');
        const inputs = form.querySelectorAll('input[required], select[required]');
        let isValid = true;

        inputs.forEach(input => {
            if (!input.value.trim()) {
                isValid = false;
                input.classList.add('border-red-500');
                if (!input.parentElement.querySelector('.error-message')) {
                    const errorEl = document.createElement('span');
                    errorEl.className = 'error-message text-red-500 text-xs mt-1 block';
                    errorEl.textContent = '此字段为必填项';
                    input.parentElement.appendChild(errorEl);
                }
            } else {
                input.classList.remove('border-red-500');
                input.parentElement.querySelector('.error-message')?.remove();
            }
        });

        if (!isValid) {
            alert('请填写所有必填字段！');
            return false;
        }
        return true;
    }

    // 编辑按钮点击事件
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('edit-btn')) {
            const btn = e.target;
            modalTitle.textContent = '编辑学生信息';

            // 填充表单数据
            document.getElementById('id').value = btn.dataset.id || '';
            document.getElementById('studentNo').value = btn.dataset.studentno || '';
            document.getElementById('name').value = btn.dataset.name || '';
            document.getElementById('gender').value = btn.dataset.gender || '';
            document.getElementById('age').value = btn.dataset.age || '';
            document.getElementById('birthDate').value = btn.dataset.birthdate || '';
            document.getElementById('phone').value = btn.dataset.phone || '';
            document.getElementById('className').value = btn.dataset.classname || '';
            document.getElementById('department').value = btn.dataset.department || '';
            document.getElementById('status').value = btn.dataset.status || '';

            studentModal.style.display = 'flex';
        }
    });

    // 添加学生按钮点击事件
    addBtn.addEventListener('click', function() {
        modalTitle.textContent = '添加学生信息';

        // 清空表单
        document.getElementById('id').value = '';
        document.getElementById('studentNo').value = '';
        document.getElementById('name').value = '';
        document.getElementById('gender').value = '';
        document.getElementById('age').value = '';
        document.getElementById('birthDate').value = '';
        document.getElementById('phone').value = '';
        document.getElementById('className').value = '';
        document.getElementById('department').value = '';
        document.getElementById('status').value = '';

        studentModal.style.display = 'flex';
    });
</script>
</body>
</html>