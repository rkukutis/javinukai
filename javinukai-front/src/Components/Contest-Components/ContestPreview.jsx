import React, { useState, useEffect } from 'react';
import axios from 'axios';

function ContestPreview() {
  const [contests, setContests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchContests = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/contests');
        setContests(response.data.content); 
        setLoading(false);
      } catch (error) {
        console.error('Error fetching contests:', error);
      }
    };

    fetchContests();
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-4">Contest Previews</h2>
      {loading ? (
        <p className="text-gray-600">Loading...</p>
      ) : (
        <ul>
          {contests.map(contest => (
            <li key={contest.id} className="border rounded-md p-4 mb-4">
              <h3 className="text-lg font-semibold mb-2">{contest.contestName}</h3>
              <p className="text-gray-600 mb-2">{contest.description}</p>
              <p className="text-gray-700 mb-2">Total Submissions: {contest.totalSubmissions}</p>
              <p className="text-gray-700 mb-2">Start Date: {new Date(contest.startDate).toLocaleDateString()}</p>
              <p className="text-gray-700 mb-2">End Date: {new Date(contest.endDate).toLocaleDateString()}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default ContestPreview;
