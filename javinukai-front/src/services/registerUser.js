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
  console.log(res);
  const data = await res.json();
  console.log(data);
  return data;
}
