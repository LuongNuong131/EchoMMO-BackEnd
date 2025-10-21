// static/js/header.js
document.addEventListener('DOMContentLoaded', () => {
    const accountBtn = document.querySelector('.account-btn'); // Cần class này trên button account
    const dropdown = document.querySelector('.dropdown');

    if (accountBtn && dropdown) {
        accountBtn.addEventListener('click', (event) => {
            event.stopPropagation(); // Ngăn click lan ra ngoài
            dropdown.classList.toggle('show');
        });

        // Đóng dropdown nếu click ra ngoài
        document.addEventListener('click', (event) => {
            if (!dropdown.contains(event.target) && !accountBtn.contains(event.target)) {
                dropdown.classList.remove('show');
            }
        });
    }

    // Xử lý active link sidebar dựa trên URL hiện tại
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar a');
    sidebarLinks.forEach(link => {
        link.classList.remove('active'); // Xóa active cũ
        // So sánh chính xác hoặc kiểm tra bắt đầu bằng nếu là trang con
        if (link.getAttribute('href') === currentPath ||
           (currentPath.startsWith(link.getAttribute('href')) && link.getAttribute('href') !== '/')) { // Check startsWith, tránh active '/' cho mọi trang
            link.classList.add('active');
        }
    });
     // Nếu không có link nào khớp chính xác, active trang chủ nếu đang ở trang chủ
     if (currentPath === '/' && !document.querySelector('.sidebar a.active')) {
        const homeLink = document.querySelector('.sidebar a[href="/"]');
        if (homeLink) homeLink.classList.add('active');
     }

});