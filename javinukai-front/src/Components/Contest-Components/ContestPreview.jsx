import React, { useState } from "react";
import axios from "axios";
import { useQuery } from "@tanstack/react-query";

function ContestPreview() {
  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState("recent");

  const { data: contests, isLoading } = useQuery({
    queryKey: ['contests', sortBy],
    queryFn: async ({ queryKey }) => {
      const [, sortBy] = queryKey;
      const response = await axios.get(`${import.meta.env.VITE_BACKEND}/api/v1/contests`);

      return response.data.content;
    },
    
  });

  const filteredContests = contests ? contests.filter((contest) =>
    contest.contestName.toLowerCase().includes(searchTerm.toLowerCase())
  ) : [];

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-4">Contest Previews</h2>
      <div className="flex justify-between mb-4">
        <div className="mb-4 flex">
          <input
            type="text"
            placeholder="Search by name..."
            className="border border-gray-300 rounded-md px-3 py-1 mr-2"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="border border-gray-300 rounded-md px-3 py-1"
          >
            <option value="recent">Recently Added</option>
            <option value="totalCategories">Most Categories</option>
          </select>
        </div>
      </div>
      {isLoading ? (
        <p className="text-gray-600">Loading...</p>
      ) : (
        <ul>
          {filteredContests.map((contest) => (
            <li key={contest.id} className="border rounded-md p-4 mb-4">
              <h3 className="text-lg font-semibold mb-2">
                {contest.contestName}
              </h3>
              <p className="text-gray-600 mb-2">{contest.description}</p>
              <p className="text-gray-700 mb-2">
                Total Submissions: {contest.totalSubmissions}
              </p>
              <p className="text-gray-700 mb-2">
                Start Date: {new Date(contest.startDate).toLocaleDateString()}
              </p>
              <p className="text-gray-700 mb-2">
                End Date: {new Date(contest.endDate).toLocaleDateString()}
              </p>
              <div className="flex flex-wrap">
                {contest.categories.map((category) => (
                  <div key={category.id} className="border rounded-md p-2 mr-2 mb-2">
                    <p className="text-sm font-semibold">{category.categoryName} - {category.type}</p>
                    <p className="text-xs">{category.description}</p>
                    <p className="text-xs">Total Submissions: {category.totalSubmissions}</p>
                  </div>
                ))}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default ContestPreview;
