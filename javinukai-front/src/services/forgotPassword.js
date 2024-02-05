async function forgotPassword(email) {
  const res = await fetch("http://localhost:8080/api/v1/auth/forgot-password", {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify({ email }),
  });
  const data = await res.text();
  return data;
}

export default forgotPassword;
