export default async function endCompetition(data) {
  const res = await fetch(
    `${import.meta.env.VITE_BACKEND}/api/v1/archive/${data.id}`,
    {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "react-front",
      },
      body: JSON.stringify(data.winners),
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
  return await res.json();
}
