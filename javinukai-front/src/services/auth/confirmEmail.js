async function confirmEmail(token) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/confirm-email?token=${token}`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  const data = await res.text();
  return data;
}

export default confirmEmail;
