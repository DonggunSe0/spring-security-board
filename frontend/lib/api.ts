const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export async function apiGet(path: string) {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "GET",
    });

    if (!response.ok) {
        throw new Error("API 요청에 실패했습니다.");
    }

    return response.text();
}