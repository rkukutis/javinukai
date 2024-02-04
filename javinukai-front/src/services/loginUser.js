export default async function (loginInfo) {
  const res = await fetch("http://localhost:8080/api/v1/auth/login", {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify(loginInfo),
  });
  const data = await res.json();
  return data;
}
