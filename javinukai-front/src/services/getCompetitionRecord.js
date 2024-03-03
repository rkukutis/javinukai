export default async function (contestId, categoryId) {
  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/records/contest/${contestId}/category/${categoryId}/my-record`,
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
