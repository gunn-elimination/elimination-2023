import Head from 'next/head';


export default function Home() {
    return (
        <div className="h-screen flex items-center justify-center bg-gradient-to-br from-orange-500 via-red-500 to-pink-500">
            <Head>
                <title>Gunn Elimination 2023</title>
                <meta name="description" content="Kill or be killed. Do you have what it takes to win?" />
                <link rel="icon" href="/favicon.ico" />
            </Head>

            <div className="bg-white rounded-lg p-6 shadow-lg w-96 space-y-2">
                <h1 className="font-bold text-xl">Hello eliminators!</h1>
                <p>A reminder of frontend tasks left to do:</p>
                <ul className="list-disc list-inside">
                    <li>Frontend auth</li>
                    <li>Displaying kill codes, targets</li>
                    <li>Input for eliminating someone</li>
                    <li>Live banner or display of the current day's restriction?</li>
                    <li>Integrated calendar?</li>
                    <li>QR codes?</li>
                    <li>Live leaderboard</li>
                </ul>
            </div>
        </div>
    )
}
