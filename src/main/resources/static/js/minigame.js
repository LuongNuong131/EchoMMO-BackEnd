// Dữ liệu CHARACTER_DATA và USER_DATA được nạp từ Thymeleaf (file index.html)
let isAdventuring = false;

// DOM References
const characterEl = document.getElementById('character');
const accessEl = document.getElementById('access');
const messageBox = document.getElementById('messageBox');
const adventureBtn = document.getElementById('adventureBtn');
const gatherBtn = document.getElementById('gatherBtn');
const restBtn = document.getElementById('restBtn');

// === UI Functions ===

function updateStatsUI(stats) {
    if (stats.health !== null) document.getElementById('playerHp').textContent = stats.health;
    if (stats.energy !== null) document.getElementById('playerEnergy').textContent = stats.energy;
    if (stats.gold !== null) document.getElementById('playerGold').textContent = stats.gold;
    
    // Cập nhật lại biến JS global (nếu cần cho logic khác)
    CHARACTER_DATA.health = stats.health;
    CHARACTER_DATA.energy = stats.energy;
    USER_DATA.gold = stats.gold;
}

function showMessage(text, duration = 3000) {
    messageBox.textContent = text;
    // (Có thể thêm animation fadeIn/fadeOut nếu muốn)
}

function randBetween(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function lockButtons(locked) {
    isAdventuring = locked;
    adventureBtn.disabled = locked;
    gatherBtn.disabled = locked;
    restBtn.disabled = locked;
}

// === Event Listeners ===

adventureBtn.addEventListener('click', async () => {
    if (isAdventuring) return;
    lockButtons(true);
    showMessage('Đang phiêu lưu...');

    try {
        // 1. Gọi API /adventure
        const response = await fetch('/api/minigame/adventure', { method: 'POST' });
        const result = await response.json();

        if (!result.success) {
            showMessage('❌ ' + result.message);
            lockButtons(false);
            return;
        }

        const data = result.data;

        // 2. Cập nhật UI (Năng lượng, Vàng, EXP)
        updateStatsUI(data.updatedStats);

        // 3. Xử lý animation
        const leftPos = randBetween(160, 400);
        accessEl.style.left = (leftPos + 80) + 'px';
        accessEl.style.opacity = '1';
        accessEl.textContent = data.encounterIcon || '❓'; // Icon

        characterEl.style.left = leftPos + 'px';
        await delay(900); // Chờ di chuyển

        accessEl.classList.add('shake');
        await delay(600);
        accessEl.classList.remove('shake');
        
        showMessage('✅ ' + data.message);

        // 4. Xử lý chuyển hướng (nếu có)
        if (data.encounterRedirect) {
            await delay(1500); // Chờ 1.5s
            location.href = data.encounterRedirect; // Chuyển trang (tới Battle hoặc Gathering)
            return; // Dừng tại đây, không cần unlock button
        }

        // 5. Quay về
        await delay(1000);
        accessEl.style.opacity = '0';
        characterEl.style.left = '48px';
        
        lockButtons(false);

    } catch (error) {
        showMessage('Lỗi kết nối: ' + error.message);
        lockButtons(false);
    }
});

gatherBtn.addEventListener('click', () => {
    // FIX: Chuyển hướng đến trang thu thập (tạm thời là stone)
    // Ở Phần 2, chúng ta sẽ làm trang này
    location.href = '/gathering/stone'; 
});

restBtn.addEventListener('click', async () => {
    if (isAdventuring) return;
    lockButtons(true);

    try {
        const response = await fetch('/api/minigame/rest', { method: 'POST' });
        const result = await response.json();

        if (result.success) {
            updateStatsUI(result.data);
            showMessage('😴 ' + result.message);
        } else {
            showMessage('⚡ ' + result.message);
        }
    } catch (error) {
         showMessage('Lỗi kết nối: ' + error.message);
    }
    
    lockButtons(false);
});

function useQuickPotion() {
    // API này đã tồn tại trong InventoryController
    // Tuy nhiên, chúng ta không biết userItemId của Potion
    // Giải pháp đúng là:
    // 1. Gọi /api/inventory/items
    // 2. Tìm item có category = 'potion'
    // 3. Lấy userItemId và gọi /inventory/use/{userItemId}
    
    showMessage('💊 Tính năng đang phát triển (cần gọi API kho đồ).');
}

// Initialize
showMessage('🎮 Bắt đầu phiêu lưu!');