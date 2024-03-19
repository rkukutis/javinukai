export default async function () {
  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/contests/latest-contest-thumbnails?limit=9`,
    {
      method: "GET",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }
  return await res.json();
}
