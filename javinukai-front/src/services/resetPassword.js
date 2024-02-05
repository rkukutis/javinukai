async function resetPassword(resetToken, newPassword) {
  console.log(resetToken + "/" + newPassword);

  const res = await fetch(`http://localhost:8080/api/v1/auth/reset-password`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify({ resetToken: resetToken, newPassword: newPassword }),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  } else {
    return await res.json();
  }
}

export default resetPassword;
