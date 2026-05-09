"use client";

import { useEffect, useState } from "react";
import { apiGet } from "@/lib/api";

export default function HomePage() {
    const [health, setHealth] = useState("확인 중...");

    useEffect(() => {
        apiGet("/api/v1/health")
            .then((result) => {
                setHealth(result);
            })
            .catch(() => {
                setHealth("API 연결 실패");
            });
    }, []);

    return (
        <main style={{ padding: 40 }}>
            <h1>Spring Security Board</h1>

            <p>백엔드 연결 상태:</p>
            <strong>{health}</strong>
        </main>
    );
}