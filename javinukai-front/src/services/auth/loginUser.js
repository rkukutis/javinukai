export default async function (loginInfo) {
  const res = await fetch(`${import.meta.env.VITE_BACKEND}/api/v1/auth/login`, {
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
  if (!res.ok) throw new Error();
  const data = await res.json();
  return data;
}
