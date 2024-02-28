async function forgotPassword(email) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/auth/forgot-password`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify({ email }),
    }
  );
  if (res.status === 429) {
    throw new Error();
  }
}

export default forgotPassword;
