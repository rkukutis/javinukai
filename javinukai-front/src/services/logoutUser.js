export default async function () {
  const res = await fetch("http://localhost:8080/api/v1/auth/logout", {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "User-Agent": "react-front",
    },
  });
}
