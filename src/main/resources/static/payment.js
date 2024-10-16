// 페이지 로딩 시 상품 목록을 가져와서 표시
window.onload = async () => {
    const productListElement = document.getElementById('product-list');
    try {
        const response = await fetch('/products'); // 백엔드에서 상품 목록을 가져오는 API 호출
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

// 결제 처리 함수 (포트원 결제 창 호출)
async function processPayment(productId, price) {
    try {
        // 백엔드 서버에 결제 요청
        const response = await fetch(`/products/pay/${productId}`, {  // 수정된 경로로 요청
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ productId, price })
        });

        if (response.ok) {
            const result = await response.json();

            // 백엔드에서 받은 결제 창 URL로 리다이렉트
            if (result.payment_url) {
                // Iamport 결제 창을 호출하는 방식
                const IMP = window.IMP; // Iamport 객체
                IMP.init('your_iamport_key'); // 본인의 Iamport 가맹점 키

                // 결제 요청 데이터 설정
                const paymentData = {
                    pg: 'html5_inicis', // PG사명
                    pay_method: 'card', // 카드 결제 예시, 휴대폰/계좌이체로도 변경 가능
                    merchant_uid: 'order_' + new Date().getTime(), // 주문번호
                    name: `상품 ${productId}`, // 상품명
                    amount: price, // 결제 금액
                    buyer_name: '홍길동', // 구매자 이름
                    buyer_tel: '010-1234-5678', // 구매자 전화번호
                    buyer_email: 'buyer@example.com', // 구매자 이메일
                    m_redirect_url: 'https://yourwebsite.com/payment/result' // 결제 완료 후 리디렉션될 URL
                };

                // 결제 요청
                IMP.request_pay(paymentData, function (rsp) {
                    if (rsp.success) {
                        alert('결제가 완료되었습니다!');
                        window.location.href = result.payment_url; // 결제 완료 후 리다이렉트
                    } else {
                        alert('결제에 실패하였습니다. 오류: ' + rsp.error_msg);
                    }
                });
                return '결제 창이 열렸습니다!';
            } else {
                throw new Error('결제 처리 실패');
            }
        } else {
            throw new Error('결제 API 호출 실패');
        }
    } catch (error) {
        console.error('결제 처리 중 오류 발생:', error);
        throw new Error('결제 처리 실패');
    }
}
