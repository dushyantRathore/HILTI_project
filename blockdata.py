#import matplotlib.pyplot as plt
from flask import Flask, render_template, jsonify
import requests
import json
import numpy as np
import time

app = Flask(__name__)


@app.route('/blockdata/<block_no>', methods=["GET", "POST"])
def index(block_no=None):
    r = requests.get("http://127.0.0.1:5000/chain").text
    r = json.loads(r)

    # print r

    # Fetch the chain length

    block_no = int(block_no)

    blocks_data = {}

    block_number = r["chain"][block_no-1]["index"]
    previous_hash = r["chain"][block_no-1]["previous_hash"]
    proof = r["chain"][block_no-1]["proof"]
    timestamp = r["chain"][block_no-1]["timestamp"]

    transactions_data = []

    for i in range(0, len(r["chain"][block_no-1]["transactions"])):
        transaction = {}
        transaction["item_id"] = r["chain"][block_no-1]["transactions"][i]["item_id"]
        transaction["item_name"] = r["chain"][block_no-1]["transactions"][i]["item_name"]
        transaction["item_quantity"] = r["chain"][block_no-1]["transactions"][i]["item_quantity"]
        transaction["item_amount"] = r["chain"][block_no-1]["transactions"][i]["item_amount"]
        transaction["timestamp"] = r["chain"][block_no-1]["transactions"][i]["timestamp"]

        transactions_data.append(transaction)

    blocks_data["block_number"] = block_number
    blocks_data["previous_hash"] = previous_hash
    blocks_data["proof"] = proof
    blocks_data["timestamp"] = timestamp
    blocks_data["transaction_lengh"] = len(r["chain"][block_no-1]["transactions"])
    blocks_data["transactions"] = transactions_data

    return render_template("BlockData.html", blocks_data=blocks_data)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8800, debug=True)
