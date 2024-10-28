// script.js

document.getElementById("inputText").addEventListener("keydown", function (event) {
    if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault(); // 기본 엔터 동작 방지
        const text = this.value.trim();

        if (text) {
            checkGrammar(text);
        }
    }
});

async function checkGrammar(text) {
    const apiUrl = "http://localhost:8080/api/grammar/check"; // 실제 API 엔드포인트
    const responseElement = document.getElementById("resultText");

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ text: text })
        });

        if (!response.ok) {
            throw new Error("API 요청에 실패했습니다.");
        }

        const result = await response.json();
        responseElement.innerText = result.correctedText;
    } catch (error) {
        console.error("Error:", error);
        responseElement.innerText = "오류가 발생했습니다. 다시 시도해 주세요.";
    }
}