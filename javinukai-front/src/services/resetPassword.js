async function resetPassword(data) {
  console.log(data);

  const res = await fetch(`http://localhost:8080/api/v1/auth/reset-password`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      "User-Agent": "react-front",
    },
    body: JSON.stringify({
      resetToken: data.token,
      newPassword: data.newPassword,
    }),
  });
  if (!res.ok) {
    const err = await res.json();
    switch (err.title) {
      case "INVALID_TOKEN_ERROR":
        throw new Error("Password reset link is invalid or expired");
      default:
        throw new Error(
          "An error has occured while trying to reset your password"
        );
    }
  }
}

export default resetPassword;
