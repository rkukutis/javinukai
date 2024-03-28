export default async function (updatedUser) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/users/${updatedUser.id}`,
    {
      method: "PATCH",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify(updatedUser),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
  return await res.json();
}
