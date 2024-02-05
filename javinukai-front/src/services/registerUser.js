export default async function (registrationInfo) {
  const res = await fetch("http://localhost:8080/api/v1/auth/register", {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify(registrationInfo),
  });
  if (!res.ok) throw new Error("Registration failed. Please try again later");
  const data = await res.json();
  return data;
}
