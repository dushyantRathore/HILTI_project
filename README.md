<h1 align="center">Hilti Chain</h1>

<p align="center">Simplified. Automated. Secured.</p>

<p align="center"><a href="#site">Check It Out!</a> | <a href="#documentation">Read the Docs</a></p>

```

  ___ ___ .__.__   __  .__          _________ .__           .__        
 /   |   \|__|  |_/  |_|__|         \_   ___ \|  |__ _____  |__| ____  
/    ~    \  |  |\   __\  |  ______ /    \  \/|  |  \\__  \ |  |/    \ 
\    Y    /  |  |_|  | |  | /_____/ \     \___|   Y  \/ __ \|  |   |  \
 \___|_  /|__|____/__| |__|          \______  /___|  (____  /__|___|  /
       \/                                   \/     \/     \/        \/ 

```

[![vido_thumbnail](https://s19.postimg.org/hbw2js9f7/Screen_Shot_2018-02-24_at_7.45.03_PM.png)](https://www.youtube.com/watch?v=uv_Aiq2_I8I)

## Abstract

Our solution is based on IoT (smart devices that automate the process) and blockchain (that ensures transparency and prevents/detects fraud). We believe our solution is awesome because:
IoT enabled smart devices that automate the process involved in the construction cycle ranging from the procurement of materials to power consumption etc.
A decentralized blockchain ensures that every transaction in the process is transparent and consistent. Any fraud or tampering would lead to inconsistent blocks, hence triggering detection of a possible illegal activity.
Smart Contracts form the core of the system. By using Smart Contracts and creating DAOs for a project, the administrative load on reporting, governance, monitoring responsibilities and transfer of risk can be reduced.

## How will our product be useful for HILTI ?

As of now, we have proposed a decentralised system of raw material and power management in the initial stages of construction process. These two test cases have been discussed below :-


### Raw Material Management System
    
As we all know, the process of raw material management is quite 
cumbersome since it requires certification from approved companies and 
individuals. Along with this, there have been multiple cases of corruption in 
this phase. So to tackle these problems, we propose a system wherein the 
entire transaction starting from shipping of raw materials to their delivery gets 
deployed on a private blockchain. Since the transaction is deployed on a 
private blockchain, it is transparent and involves a consensus mechanism for 
execution and validation using the digital identity of the concerned people 
from the company. 

We have also tried to reduce the manual intervention involved in the process by deploying the entire payment system 
to the blockchain network using Smart Contracts which can be triggered automatically.

### Power Management System

The main problems with the existing infrastructure are :-

- Billing process requires manual intervention.
- Electricity bill may not be paid on time by the buyer.
- Unethical management of units by the concerned power board.

All these problems can be catered making use of the blockchain and DAPP 
(Decentralised Applications) technology.

## Instructions to run

**1. Procurement Process**

On seperate command line windows, execute the following commands in the `/procurement` directory

```sh
python3 blockchain.py
```
This file contains main code for the blockchain. 

```sh
python3 blockdata.py
```
This file contains the code for block explorer which is used to visualise mined blocks and transactions.

Install `ngrok`, tunnel the port 5000 using ngrok to get a url. Here's the command for it,
```sh
./ngrok http 5000
```
Note: Command might vary for Windows platform

Copy the url given by ngrok and update it in android app's source code, [here](https://github.com/dushyantRathore/HILTI_project/blob/master/Android/Procurement-App/app/src/main/java/com/fingerprint/sample/DecoderActivity.java#L59). This registration of app with a blockchain node, is just a one time process and need not to be done when running again.

Now build and install the android application (Hilti-Commerce) and submit an order for raw materials. (App is available in the `/Android directory`)

Build and install Procurement-App and verify and approve the order with your identity. (App is available in the `/Android directory`) You'll receive the transaction hash as a proof, and the distributor will be notified via the SMS.

Now,
```sh
python3 Graph.py
```
Graph.py serves as the home page for the block explorer.

Open the link "http://localhost:8000" in browser of your choice and the block explorer should be up and running. You can click the individual transactions to verify their contents.

2. Resource Management

On seperate command line windows, execute the following commands in the `/resource_management` directory

```sh
python3 blockchain.py
```

```sh
python3 smartcontract.py
```
This file implements the smart contract and payment system.

```sh
python3 upload_units_raspi.py & python paymentchain.py
```
upload_units_raspi.py uploads the sample units from Raspberry pi to blockchain network. 

Install `ngrok`, tunnel the port 5000 using ngrok to get a url. Here's the command for it,
```sh
./ngrok http 5000
```
Note: Command might vary for Windows platform

Copy the url given by ngrok and update it in android app's source code, [here](https://github.com/dushyantRathore/HILTI_project/blob/master/Android/Smart-power-management-Flash/app/src/main/java/com/flash/brainbreaker/flash/MainActivity.java#L42) and [here](https://github.com/dushyantRathore/HILTI_project/blob/master/Android/Smart-power-management-Flash/app/src/main/java/com/flash/brainbreaker/flash/PaymentActivity.java#L35). This registration with a blockchain node, is just a one time process and need not to be done when running again.

Now build and install the android application (Smart Power Management Flash) and connect the device to the power port. (App is available in the `/Android directory`)

Sign in with any details (use meter ID as 1x11 for now) and query for the bill and make the payment.

```sh
python3 Graph.py
```

Open the link "http://localhost:8000" in browser of your choice and the block explorer should be up and running.

## License

MIT License - see the [LICENSE](https://github.com/dushyantRathore/HILTI_project/blob/master/LICENSE) file for details
