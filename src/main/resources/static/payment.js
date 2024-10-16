// 페이지 로딩 시 상품 목록을 가져와서 표시
window.onload = async () => {
    const productListElement = document.getElementById('product-list');
    try {
        const response = await fetch('/products'); // 백엔드에서 상품 목록을 가져오는 API
        const products = await response.json();

        if (products && products.length > 0) {
            products.forEach(product => {
                const productDiv = document.createElement('div');
                productDiv.classList.add('product');

                productDiv.innerHTML = `
                    <h2>${product.name}</h2>
                    <p>가격: ${product.price}원</p>
                    <button class="pay-button" data-id="${product.id}" data-price="${product.price}">결제하기</button>
                `;

                productListElement.appendChild(productDiv);
            });

            // 결제 버튼에 이벤트 리스너 추가
            const payButtons = document.querySelectorAll('.pay-button');
            payButtons.forEach(button => {
                button.addEventListener('click', async (e) => {
                    const productId = e.target.getAttribute('data-id');
                    const price = e.target.getAttribute('data-price');

                    try {
                        const result = await processPayment(productId, price);
                        alert(result); // 결제 성공 시 알림
                    } catch (error) {
                        alert('결제 실패! 다시 시도해주세요.');
                    }
                });
            });
        } else {
            productListElement.innerHTML = '<p>상품이 없습니다.</p>';
        }
    } catch (error) {
        console.error('상품 목록을 가져오는 중 오류가 발생했습니다.', error);
    }
};

// 결제 처리 함수
async function processPayment(productId, price) {
    const response = await fetch(`/products/pay/${productId}`, { // 백엔드 결제 API 호출
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (response.ok) {
        return '결제가 성공적으로 완료되었습니다!';
    } else {
        throw new Error('결제 처리 실패');
    }
}
